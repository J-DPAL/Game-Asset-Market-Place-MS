package pallares.gameassetmarketplace.users.presentationLayer;

import pallares.gameassetmarketplace.users.businessLayer.UserService;
import pallares.gameassetmarketplace.users.utils.exceptions.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private static final int UUID_LENGTH = 36;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseModel>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseModel> getUserByUserId(@PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userIdentifier provided: " + userId);
        }
        return ResponseEntity.ok().body(userService.getUserByUserId(userId));
    }

    @PostMapping()
    public ResponseEntity<UserResponseModel> addUser(@RequestBody UserRequestModel userRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userRequestModel));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseModel> updateUser(@RequestBody UserRequestModel userRequestModel, @PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userIdentifier provided: " + userId);
        }
        return ResponseEntity.ok().body(userService.updateUser(userRequestModel, userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userIdentifier provided: " + userId);
        }
       userService.removeUser(userId);
       return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
