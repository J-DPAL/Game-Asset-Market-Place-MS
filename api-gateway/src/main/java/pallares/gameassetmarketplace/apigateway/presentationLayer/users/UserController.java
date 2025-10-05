package pallares.gameassetmarketplace.apigateway.presentationLayer.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pallares.gameassetmarketplace.apigateway.businessLayer.users.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/v1/users/{userId}
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseModel> getUserByUserId(@PathVariable String userId) {
        log.debug("1. Request Received in API-Gateway Users Controller: getUserByUserId");
        return ResponseEntity.ok(userService.getUserByUserId(userId));
    }

    // GET /api/v1/users
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponseModel>> getAllUsers() {
        log.debug("2. Request Received in API-Gateway Users Controller: getAllUsers");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // POST /api/v1/users
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseModel> addUser(@RequestBody UserRequestModel userRequestModel) {
        log.debug("3. Request Received in API-Gateway Users Controller: addUser");
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userRequestModel));
    }

    // PUT /api/v1/users/{userId}
    @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseModel> updateUser(@PathVariable String userId,
                                                                  @RequestBody UserRequestModel userRequestModel) {
        log.debug("4. Request Received in API-Gateway Users Controller: updateUser");
        return ResponseEntity.ok(userService.updateUser(userRequestModel, userId));
    }

    // DELETE /api/v1/users/{userId}
    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseModel> removeUser(@PathVariable String userId) {
        log.debug("5. Request Received in API-Gateway Users Controller: removeUser");
        userService.removeUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
