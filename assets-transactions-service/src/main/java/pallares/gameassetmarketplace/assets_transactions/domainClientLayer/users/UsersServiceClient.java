package pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pallares.gameassetmarketplace.assets_transactions.utils.HttpErrorInfo;
import pallares.gameassetmarketplace.assets_transactions.utils.exceptions.InvalidInputException;
import pallares.gameassetmarketplace.assets_transactions.utils.exceptions.NotFoundException;

import java.io.IOException;

@Slf4j
@Component
public class UsersServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String USER_SERVICE_BASE_URL;

    public UsersServiceClient(RestTemplate restTemplate,
                              ObjectMapper mapper,
                              @Value("${app.users-service.host}") String usersServiceHost,
                              @Value("${app.users-service.port}") String usersServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.USER_SERVICE_BASE_URL = "http://" + usersServiceHost + ":" + usersServicePort + "/api/v1/users";
    }

    public UserModel getUserByUserId(String userId) {
        try {
            String url = USER_SERVICE_BASE_URL + "/" + userId;
            log.debug("GET User URL: {}", url);
            return restTemplate.getForObject(url, UserModel.class);
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
