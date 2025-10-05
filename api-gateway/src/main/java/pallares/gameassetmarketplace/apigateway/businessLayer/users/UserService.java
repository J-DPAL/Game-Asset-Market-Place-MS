package pallares.gameassetmarketplace.apigateway.businessLayer.users;

import org.springframework.stereotype.Service;
import pallares.gameassetmarketplace.apigateway.presentationLayer.users.UserRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.users.UserResponseModel;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {

    List<UserResponseModel> getAllUsers();
    UserResponseModel getUserByUserId(String userId);
    UserResponseModel addUser(UserRequestModel userRequestModel);
    UserResponseModel updateUser(UserRequestModel updatedUser, String userId);
    void removeUser(String userId);
}
