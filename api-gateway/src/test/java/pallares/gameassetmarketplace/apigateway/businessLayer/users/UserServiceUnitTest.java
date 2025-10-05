package pallares.gameassetmarketplace.apigateway.businessLayer.users;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pallares.gameassetmarketplace.apigateway.domainclientLayer.users.UsersServiceClient;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.users.UserRoleEnum;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.users.PhoneNumber;
import pallares.gameassetmarketplace.apigateway.presentationLayer.users.UserRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.users.UserResponseModel;

class UserServiceUnitTest {

    @Mock
    private UsersServiceClient usersServiceClient;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestModel sampleRequest;
    private UserResponseModel sampleResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleRequest = new UserRequestModel(
                "John",
                "Doe",
                "johndoe",
                "john@example.com",
                UserRoleEnum.SELLER,
                new PhoneNumber("1234567890"),
                LocalDate.of(2025, 1, 1)
        );

        sampleResponse = UserResponseModel.builder()
                .userId("user-abc")
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .emailAddress("john@example.com")
                .userRoleEnum(UserRoleEnum.SELLER)
                .phoneNumber(new PhoneNumber("1234567890"))
                .accountCreationDate(LocalDate.of(2025, 1, 1))
                .build();
    }

    @Test
    void getAllUsers_delegatesToClient() {
        when(usersServiceClient.getUsers()).thenReturn(List.of(sampleResponse));

        var result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertSame(sampleResponse, result.get(0));
        verify(usersServiceClient).getUsers();
    }

    @Test
    void getUserById_delegatesToClient() {
        when(usersServiceClient.getUserByUserId("user-abc")).thenReturn(sampleResponse);

        var result = userService.getUserByUserId("user-abc");

        assertEquals("user-abc", result.getUserId());
        verify(usersServiceClient).getUserByUserId("user-abc");
    }

    @Test
    void addUser_delegatesToClient() {
        when(usersServiceClient.addUser(sampleRequest)).thenReturn(sampleResponse);

        var result = userService.addUser(sampleRequest);

        assertSame(sampleResponse, result);
        verify(usersServiceClient).addUser(sampleRequest);
    }

    @Test
    void updateUser_invokesUpdateThenRetrieve() {
        doNothing().when(usersServiceClient).updateUser("user-abc", sampleRequest);
        when(usersServiceClient.getUserByUserId("user-abc")).thenReturn(sampleResponse);

        var result = userService.updateUser(sampleRequest, "user-abc");

        assertSame(sampleResponse, result);
        InOrder inOrder = inOrder(usersServiceClient);
        inOrder.verify(usersServiceClient).updateUser("user-abc", sampleRequest);
        inOrder.verify(usersServiceClient).getUserByUserId("user-abc");
    }

    @Test
    void removeUser_delegatesToClient() {
        doNothing().when(usersServiceClient).removeUser("user-abc");

        userService.removeUser("user-abc");

        verify(usersServiceClient).removeUser("user-abc");
    }
}