package pallares.gameassetmarketplace.apigateway.domainclientLayer.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pallares.gameassetmarketplace.apigateway.presentationLayer.users.UserRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.users.UserResponseModel;
import pallares.gameassetmarketplace.apigateway.utils.HttpErrorInfo;
import pallares.gameassetmarketplace.apigateway.utils.exceptions.InvalidInputException;
import pallares.gameassetmarketplace.apigateway.utils.exceptions.NotFoundException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UsersServiceClientUnitTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private UsersServiceClient client;

    private final String baseUrl = "http://host:1234/api/v1/users";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Manually inject the base URL via constructor
        client = new UsersServiceClient(restTemplate, mapper, "host", "1234");
    }

    @Test
    void getUserByUserId_success() {
        var now = LocalDate.now();
        var expected = new UserResponseModel(
                "u1",          // userId
                "Jane",        // firstName
                "Doe",         // lastName
                "jdoe",        // username
                "jdoe@example.com", // emailAddress
                null,          // userRoleEnum
                null,          // phoneNumber
                now            // accountCreationDate
        );
        when(restTemplate.getForObject(baseUrl + "/u1", UserResponseModel.class))
                .thenReturn(expected);

        var actual = client.getUserByUserId("u1");

        assertSame(expected, actual);
        verify(restTemplate).getForObject(baseUrl + "/u1", UserResponseModel.class);
    }

    @Test
    void getUserByUserId_notFound_raisesNotFoundException() throws IOException {
        String body = "{\"message\":\"User not found\"}";
        var ex = HttpClientErrorException.create(
                HttpStatus.NOT_FOUND,
                "404",
                null,
                body.getBytes(),
                null
        );
        when(restTemplate.getForObject(anyString(), eq(UserResponseModel.class))).thenThrow(ex);
        when(mapper.readValue(body, HttpErrorInfo.class))
                .thenReturn(new HttpErrorInfo(HttpStatus.NOT_FOUND, "/u1", "User not found"));

        var thrown = assertThrows(NotFoundException.class, () -> client.getUserByUserId("u1"));
        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void getUsers_success() {
        var now = LocalDate.now();
        var array = new UserResponseModel[]{
                new UserResponseModel("u1","A","B","ab","ab@example.com", null, null, now),
                new UserResponseModel("u2","C","D","cd","cd@example.com", null, null, now)
        };
        when(restTemplate.getForEntity(baseUrl, UserResponseModel[].class))
                .thenReturn(new ResponseEntity<>(array, HttpStatus.OK));

        var list = client.getUsers();
        assertEquals(2, list.size());
        assertEquals("u1", list.get(0).getUserId());
    }

    @Test
    void getUsers_badRequest_raisesInvalidInputException() throws IOException {
        String body = "{\"message\":\"Invalid request\"}";
        var ex = HttpClientErrorException.create(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "422",
                null,
                body.getBytes(),
                null
        );
        when(restTemplate.getForEntity(anyString(), eq(UserResponseModel[].class)))
                .thenThrow(ex);
        when(mapper.readValue(body, HttpErrorInfo.class))
                .thenReturn(new HttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, "/", "Invalid request"));

        var thrown = assertThrows(InvalidInputException.class, () -> client.getUsers());
        assertEquals("Invalid request", thrown.getMessage());
    }

    @Test
    void addUser_success() {
        var now = LocalDate.now();
        var req = new UserRequestModel("Jane","Doe","jdoe","jdoe@example.com", null, null, now);
        var resp = new UserResponseModel("u1","Jane","Doe","jdoe","jdoe@example.com", null, null, now);
        when(restTemplate.postForObject(baseUrl, req, UserResponseModel.class))
                .thenReturn(resp);

        var result = client.addUser(req);
        assertSame(resp, result);
    }

    @Test
    void updateUser_success() {
        var now = LocalDate.now();
        var req = new UserRequestModel("Jane","Doe","jdoe","jdoe@example.com", null, null, now);
        doNothing().when(restTemplate).put(baseUrl + "/u1", req);

        assertDoesNotThrow(() -> client.updateUser("u1", req));
        verify(restTemplate).put(baseUrl + "/u1", req);
    }

    @Test
    void removeUser_success() {
        doNothing().when(restTemplate).delete(baseUrl + "/u1");

        assertDoesNotThrow(() -> client.removeUser("u1"));
        verify(restTemplate).delete(baseUrl + "/u1");
    }
}
