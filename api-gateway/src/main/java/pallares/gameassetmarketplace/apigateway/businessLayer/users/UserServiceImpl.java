package pallares.gameassetmarketplace.apigateway.businessLayer.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pallares.gameassetmarketplace.apigateway.businessLayer.users.UserService;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.users.UsersServiceClient;
import pallares.gameassetmarketplace.apigateway.presentationLayer.users.UserRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.users.UserResponseModel;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UsersServiceClient usersServiceClient;

    @Autowired
    public UserServiceImpl(UsersServiceClient usersServiceClient) {
        this.usersServiceClient = usersServiceClient;
    }

    @Override
    public UserResponseModel getUserByUserId(String userId) {
        log.debug("Fetching user with ID: {}", userId);
        return usersServiceClient.getUserByUserId(userId);
    }

    @Override
    public List<UserResponseModel> getAllUsers() {
        log.debug("Fetching all users");
        return usersServiceClient.getUsers();
    }

    @Override
    public UserResponseModel addUser(UserRequestModel userRequestModel) {
        log.debug("Creating new user: {}", userRequestModel);
        return usersServiceClient.addUser(userRequestModel);
    }

    @Override
    public UserResponseModel updateUser(UserRequestModel userRequestModel, String userId) {
        log.debug("Updating user with ID: {}", userId);
        usersServiceClient.updateUser(userId, userRequestModel);
        return usersServiceClient.getUserByUserId(userId);
    }

    @Override
    public void removeUser(String userId) {
        log.debug("Deleting user with ID: {}", userId);
        usersServiceClient.removeUser(userId);
    }
}
