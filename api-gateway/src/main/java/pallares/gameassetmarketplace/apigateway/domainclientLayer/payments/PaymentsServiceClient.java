package pallares.gameassetmarketplace.apigateway.domainclientLayer.payments;

import pallares.gameassetmarketplace.apigateway.presentationLayer.payments.PaymentRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.payments.PaymentResponseModel;
import pallares.gameassetmarketplace.apigateway.utils.HttpErrorInfo;
import pallares.gameassetmarketplace.apigateway.utils.exceptions.InvalidInputException;
import pallares.gameassetmarketplace.apigateway.utils.exceptions.NotFoundException;
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

    public PaymentResponseModel getPaymentByPaymentId(String paymentId) {
        try {
            String url = PAYMENT_SERVICE_BASE_URL + "/" + paymentId;
            log.debug("GET Payment URL: {}", url);
            return restTemplate.getForObject(url, PaymentResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public List<PaymentResponseModel> getPayments() {
        try {
            log.debug("GET All Payments URL: {}", PAYMENT_SERVICE_BASE_URL);
            ResponseEntity<PaymentResponseModel[]> response = restTemplate.getForEntity(PAYMENT_SERVICE_BASE_URL, PaymentResponseModel[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public PaymentResponseModel addPayment(PaymentRequestModel paymentRequestModel) {
        try {
            log.debug("POST Create Payment URL: {}", PAYMENT_SERVICE_BASE_URL);
            return restTemplate.postForObject(PAYMENT_SERVICE_BASE_URL, paymentRequestModel, PaymentResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void updatePayment(String paymentId, PaymentRequestModel paymentRequestModel) {
        try {
            String url = PAYMENT_SERVICE_BASE_URL + "/" + paymentId;
            log.debug("PUT Update Payment URL: {}", url);
            restTemplate.put(url, paymentRequestModel);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void removePayment(String paymentId) {
        try {
            String url = PAYMENT_SERVICE_BASE_URL + "/" + paymentId;
            log.debug("DELETE Payment URL: {}", url);
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
