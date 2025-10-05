package pallares.gameassetmarketplace.apigateway.presentationLayer.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pallares.gameassetmarketplace.apigateway.businessLayer.users.UserService;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.users.PhoneNumber;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.users.UserRoleEnum;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerUnitTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserRequestModel sampleRequest;
    private UserResponseModel sampleResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleRequest = new UserRequestModel(
                "Alice",
                "Smith",
                "asmith",
                "alice@example.com",
                UserRoleEnum.BUYER,
                new PhoneNumber("123-456-7890"),
                LocalDate.of(2024, 1, 1)
        );

        sampleResponse = UserResponseModel.builder()
                .userId("u123")
                .firstName("Alice")
                .lastName("Smith")
                .username("asmith")
                .emailAddress("alice@example.com")
                .userRoleEnum(UserRoleEnum.ADMIN)
                .phoneNumber(new PhoneNumber("123-456-7890"))
                .accountCreationDate(LocalDate.of(2024, 1, 1))
                .build();
    }

    @Test
    void getUserByUserId_ReturnsUser() {
        when(userService.getUserByUserId("u123")).thenReturn(sampleResponse);

        ResponseEntity<UserResponseModel> response = userController.getUserByUserId("u123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleResponse, response.getBody());
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        List<UserResponseModel> users = Arrays.asList(sampleResponse);
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UserResponseModel>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void addUser_ReturnsCreatedUser() {
        when(userService.addUser(sampleRequest)).thenReturn(sampleResponse);

        ResponseEntity<UserResponseModel> response = userController.addUser(sampleRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleResponse, response.getBody());
    }

    @Test
    void updateUser_ReturnsUpdatedUser() {
        when(userService.updateUser(sampleRequest, "u123")).thenReturn(sampleResponse);

        ResponseEntity<UserResponseModel> response = userController.updateUser("u123", sampleRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleResponse, response.getBody());
    }

    @Test
    void removeUser_ReturnsNoContent() {
        doNothing().when(userService).removeUser("u123");

        ResponseEntity<UserResponseModel> response = userController.removeUser("u123");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
