package pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets;

import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.AssetModel;
import pallares.gameassetmarketplace.assets_transactions.utils.HttpErrorInfo;
import pallares.gameassetmarketplace.assets_transactions.utils.exceptions.InvalidInputException;
import pallares.gameassetmarketplace.assets_transactions.utils.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class AssetsServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String ASSET_SERVICE_BASE_URL;

    public AssetsServiceClient(RestTemplate restTemplate,
                               ObjectMapper mapper,
                               @Value("${app.assets-service.host}") String assetsServiceHost,
                               @Value("${app.assets-service.port}") String assetsServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.ASSET_SERVICE_BASE_URL = "http://" + assetsServiceHost + ":" + assetsServicePort + "/api/v1/assets";
    }

    public AssetModel getAssetByAssetId(String assetId) {
        try {
            String url = ASSET_SERVICE_BASE_URL + "/" + assetId;
            log.debug("GET Asset URL: {}", url);
            return restTemplate.getForObject(url, AssetModel.class);
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
