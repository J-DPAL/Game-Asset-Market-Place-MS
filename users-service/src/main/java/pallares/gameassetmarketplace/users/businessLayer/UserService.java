package pallares.gameassetmarketplace.users.businessLayer;

import org.springframework.stereotype.Service;
import pallares.gameassetmarketplace.users.presentationLayer.UserRequestModel;
import pallares.gameassetmarketplace.users.presentationLayer.UserResponseModel;

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
