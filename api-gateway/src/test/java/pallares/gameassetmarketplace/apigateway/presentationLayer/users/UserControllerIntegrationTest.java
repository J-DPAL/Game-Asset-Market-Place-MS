package pallares.gameassetmarketplace.apigateway.presentationLayer.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import pallares.gameassetmarketplace.apigateway.businessLayer.users.UserService;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.users.PhoneNumber;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.users.UserRoleEnum;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest(controllers = UserController.class)
class UserControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserService userService;

    private UserResponseModel mockUser;
    private UserRequestModel request;

    private static final String BASE_URL = "/api/v1/users";

    @BeforeEach
    void setUp() {
        mockUser = UserResponseModel.builder()
                .userId("user-1")
                .firstName("Jane")
                .lastName("Doe")
                .username("janedoe")
                .emailAddress("jane@example.com")
                .userRoleEnum(UserRoleEnum.BUYER)
                .phoneNumber(new PhoneNumber("5141234567"))
                .accountCreationDate(LocalDate.of(2023, 1, 1))
                .build();

        request = new UserRequestModel(
                "Jane", "Doe", "janedoe", "jane@example.com",
                UserRoleEnum.BUYER, new PhoneNumber("5141234567"),
                LocalDate.of(2023, 1, 1)
        );
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(mockUser));

        webTestClient.get()
                .uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponseModel.class).hasSize(1);

        verify(userService).getAllUsers();
    }

    @Test
    void getUserById_ReturnsUser() {
        when(userService.getUserByUserId("user-1")).thenReturn(mockUser);

        webTestClient.get()
                .uri(BASE_URL + "/user-1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.userId").isEqualTo("user-1");

        verify(userService).getUserByUserId("user-1");
    }

    @Test
    void createUser_ReturnsCreatedUser() {
        when(userService.addUser(any(UserRequestModel.class))).thenReturn(mockUser);

        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.userId").isEqualTo("user-1")
                .jsonPath("$.username").isEqualTo("janedoe");

        verify(userService).addUser(any(UserRequestModel.class));
    }


    @Test
    void updateUser_ReturnsUpdatedUser() {
        when(userService.updateUser(any(UserRequestModel.class), eq("user-1"))).thenReturn(mockUser);

        webTestClient.put()
                .uri(BASE_URL + "/user-1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.userId").isEqualTo("user-1");

        verify(userService).updateUser(any(UserRequestModel.class), eq("user-1"));
    }


    @Test
    void deleteUser_ReturnsNoContent() {
        doNothing().when(userService).removeUser("user-1");

        webTestClient.delete()
                .uri(BASE_URL + "/user-1")
                .exchange()
                .expectStatus().isNoContent();

        verify(userService).removeUser("user-1");
    }
}
