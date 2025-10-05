package pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions.AssetTransactionRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions.AssetTransactionResponseModel;
import pallares.gameassetmarketplace.apigateway.utils.HttpErrorInfo;
import pallares.gameassetmarketplace.apigateway.utils.exceptions.InvalidInputException;
import pallares.gameassetmarketplace.apigateway.utils.exceptions.NotFoundException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class AssetTransactionServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String TRANSACTIONS_SERVICE_BASE_URL;

    public AssetTransactionServiceClient(RestTemplate restTemplate,
                                         ObjectMapper mapper,
                                         @Value("${app.assets-transactions-service.host}") String serviceHost,
                                         @Value("${app.assets-transactions-service.port}") String servicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.TRANSACTIONS_SERVICE_BASE_URL = "http://" + serviceHost + ":" + servicePort + "/api/v1/users";
    }

    public List<AssetTransactionResponseModel> getAllAssetTransactions(String userId) {
        try {
            String url = TRANSACTIONS_SERVICE_BASE_URL + "/" + userId + "/asset_transactions";
            log.debug("GET All AssetTransactions URL: {}", url);
            ResponseEntity<AssetTransactionResponseModel[]> response =
                    restTemplate.getForEntity(url, AssetTransactionResponseModel[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public AssetTransactionResponseModel getAssetTransactionById(String txId, String userId) {
        try {
            String url = TRANSACTIONS_SERVICE_BASE_URL + "/" + userId + "/asset_transactions/" + txId;
            log.debug("GET AssetTransaction URL: {}", url);
            return restTemplate.getForObject(url, AssetTransactionResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public AssetTransactionResponseModel addAssetTransaction(AssetTransactionRequestModel requestModel, String userId) {
        try {
            String url = TRANSACTIONS_SERVICE_BASE_URL + "/" + userId + "/asset_transactions";
            log.debug("POST Create AssetTransaction URL: {}", url);
            return restTemplate.postForObject(url, requestModel, AssetTransactionResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public AssetTransactionResponseModel updateAssetTransaction(AssetTransactionRequestModel requestModel, String txId, String userId) {
        try {
            String url = TRANSACTIONS_SERVICE_BASE_URL + "/" + userId + "/asset_transactions/" + txId;
            log.debug("PUT Update AssetTransaction URL: {}", url);
            restTemplate.put(url, requestModel);
            // many APIs return 204 No Content on PUT; fetch the updated resource
            return getAssetTransactionById(txId, userId);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void removeAssetTransaction(String txId, String userId) {
        try {
            String url = TRANSACTIONS_SERVICE_BASE_URL + "/" + userId + "/asset_transactions/" + txId;
            log.debug("DELETE AssetTransaction URL: {}", url);
            restTemplate.delete(url);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ioex.getMessage();
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
            return new InvalidInputException(getErrorMessage(ex));
        }

        log.warn("Unexpected HTTP error: {}, rethrowing", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }
}
