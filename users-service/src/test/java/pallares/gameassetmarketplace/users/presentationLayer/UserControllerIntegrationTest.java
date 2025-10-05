package pallares.gameassetmarketplace.users.presentationLayer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import pallares.gameassetmarketplace.users.dataAccessLayer.UserRepository;
import pallares.gameassetmarketplace.users.dataAccessLayer.UserRoleEnum;
import pallares.gameassetmarketplace.users.dataAccessLayer.PhoneNumber;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("h2")
@Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    private final String BASE_URL_USERS = "/api/v1/users";
    private final String VALID_USER_UUID = "c5440a89-cb47-4d96-888e-yy96708db4d9";
    private final String INVALID_USER_UUID = "550e8400-e29b-41d4-a716-4466554400";
    private final String NOTFOUND_USER_UUID = "11111111-2222-3333-4444-555566667777";

    @Test
    public void whenUsersExist_thenReturnAllUsers() {
        long sizeDb = userRepository.count();

        webTestClient.get()
                .uri(BASE_URL_USERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(UserResponseModel.class)
                .value(list -> {
                    assertNotNull(list);
                    assertEquals(sizeDb, list.size());
                });
    }

    @Test
    public void whenUserRequestIsValid_thenCreateAndReturnUser() {
        UserRequestModel userRequestModel = UserRequestModel.builder()
                .firstName("Jane")
                .lastName("Doe")
                .username("janedoe")
                .emailAddress("jane.doe@example.com")
                .userRoleEnum(UserRoleEnum.SELLER)
                .phoneNumber(new PhoneNumber("123-456-7890"))
                .accountCreationDate(LocalDate.now())
                .build();

        webTestClient.post()
                .uri(BASE_URL_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserResponseModel.class)
                .value(userResponseModel -> {
                    assertNotNull(userResponseModel);
                    assertEquals(userRequestModel.getUsername(), userResponseModel.getUsername());
                });
    }

    @Test
    public void whenUserIdIsValid_thenReturnUser() {
        webTestClient.get()
                .uri(BASE_URL_USERS + "/" + VALID_USER_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.userId").isEqualTo(VALID_USER_UUID);
    }

    @Test
    public void whenUserIdIsInvalid_thenReturnUnprocessableEntity() {
        webTestClient.get()
                .uri(BASE_URL_USERS + "/" + INVALID_USER_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid userIdentifier provided: " + INVALID_USER_UUID);
    }

    @Test
    public void whenUserExistsOnDelete_thenReturnNoContent() {
        webTestClient.delete()
                .uri(BASE_URL_USERS + "/" + VALID_USER_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri(BASE_URL_USERS + "/" + VALID_USER_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Provided userIdentifier not found: " + VALID_USER_UUID);
    }

    @Test
    public void whenUserEmailInvalid_thenReturnBadRequest() {
        // Arrange
        UserRequestModel userRequestModel = UserRequestModel.builder()
                .firstName("Invalid")
                .lastName("Email")
                .username("invalidemail")
                .emailAddress("not-an-email") // Invalid!
                .userRoleEnum(UserRoleEnum.BUYER)
                .phoneNumber(new PhoneNumber("999-888-7777"))
                .accountCreationDate(LocalDate.now())
                .build();

        // Act + Assert
        webTestClient.post()
                .uri(BASE_URL_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userRequestModel)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void whenUserIsUpdated_thenReturnUpdatedUser() {
        // Arrange
        String updatedFirstName = "UpdatedName";
        UserRequestModel updatedUser = UserRequestModel.builder()
                .firstName(updatedFirstName)
                .lastName("Last")
                .username("updateduser")
                .emailAddress("updated@example.com")
                .userRoleEnum(UserRoleEnum.SELLER)
                .phoneNumber(new PhoneNumber("123-456-7899"))
                .accountCreationDate(LocalDate.now())
                .build();

        // Act + Assert
        webTestClient.put()
                .uri(BASE_URL_USERS + "/" + VALID_USER_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(updatedUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseModel.class)
                .value(response -> assertEquals(updatedFirstName, response.getFirstName()));
    }

    @Test
    public void whenDeletingNonexistentUser_thenReturnNotFound() {
        // Act + Assert
        webTestClient.delete()
                .uri(BASE_URL_USERS + "/" + NOTFOUND_USER_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Provided userIdentifier not found: " + NOTFOUND_USER_UUID);
    }

    @Test
    public void whenDeletingInvalidUserId_thenReturnUnprocessableEntity() {
        // Act + Assert
        webTestClient.delete()
                .uri(BASE_URL_USERS + "/" + INVALID_USER_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid userIdentifier provided: " + INVALID_USER_UUID);
    }

    @Test
    public void whenUpdatingInvalidUserId_thenReturnUnprocessableEntity() {
        UserRequestModel updatedUser = UserRequestModel.builder()
                .firstName("updatedFirstName")
                .lastName("Last")
                .username("updateduser")
                .emailAddress("updated@example.com")
                .userRoleEnum(UserRoleEnum.SELLER)
                .phoneNumber(new PhoneNumber("123-456-7899"))
                .accountCreationDate(LocalDate.now())
                .build();

        // Act + Assert
        webTestClient.put()
                .uri(BASE_URL_USERS + "/" + INVALID_USER_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(updatedUser)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid userIdentifier provided: " + INVALID_USER_UUID);
    }

}
