package pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments;

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
public class PaymentsServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String PAYMENT_SERVICE_BASE_URL;

    public PaymentsServiceClient(RestTemplate restTemplate,
                                 ObjectMapper mapper,
                                 @Value("${app.payments-service.host}") String paymentsServiceHost,
                                 @Value("${app.payments-service.port}") String paymentsServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.PAYMENT_SERVICE_BASE_URL = "http://" + paymentsServiceHost + ":" + paymentsServicePort + "/api/v1/payments";
    }

    public PaymentModel getPaymentByPaymentId(String paymentId) {
        try {
            String url = PAYMENT_SERVICE_BASE_URL + "/" + paymentId;
            log.debug("GET Payment URL: {}", url);
            return restTemplate.getForObject(url, PaymentModel.class);
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
