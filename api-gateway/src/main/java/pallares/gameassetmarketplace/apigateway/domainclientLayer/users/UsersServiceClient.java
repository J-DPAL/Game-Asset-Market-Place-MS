package pallares.gameassetmarketplace.apigateway.domainclientLayer.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pallares.gameassetmarketplace.apigateway.presentationLayer.users.UserRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.users.UserResponseModel;
import pallares.gameassetmarketplace.apigateway.utils.HttpErrorInfo;
import pallares.gameassetmarketplace.apigateway.utils.exceptions.InvalidInputException;
import pallares.gameassetmarketplace.apigateway.utils.exceptions.NotFoundException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

    public UserResponseModel getUserByUserId(String userId) {
        try {
            String url = USER_SERVICE_BASE_URL + "/" + userId;
            log.debug("GET User URL: {}", url);
            return restTemplate.getForObject(url, UserResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public List<UserResponseModel> getUsers() {
        try {
            log.debug("GET All Users URL: {}", USER_SERVICE_BASE_URL);
            ResponseEntity<UserResponseModel[]> response = restTemplate.getForEntity(USER_SERVICE_BASE_URL, UserResponseModel[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public UserResponseModel addUser(UserRequestModel userRequestModel) {
        try {
            log.debug("POST Create User URL: {}", USER_SERVICE_BASE_URL);
            return restTemplate.postForObject(USER_SERVICE_BASE_URL, userRequestModel, UserResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void updateUser(String userId, UserRequestModel userRequestModel) {
        try {
            String url = USER_SERVICE_BASE_URL + "/" + userId;
            log.debug("PUT Update User URL: {}", url);
            restTemplate.put(url, userRequestModel);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void removeUser(String userId) {
        try {
            String url = USER_SERVICE_BASE_URL + "/" + userId;
            log.debug("DELETE User URL: {}", url);
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
