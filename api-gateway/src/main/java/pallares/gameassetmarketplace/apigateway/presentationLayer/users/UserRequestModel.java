package pallares.gameassetmarketplace.apigateway.presentationLayer.users;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.users.PhoneNumber;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.users.UserRoleEnum;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestModel {
    String firstName;
    String lastName;
    String username;
    String emailAddress;
    UserRoleEnum userRoleEnum;
    PhoneNumber phoneNumber;
    LocalDate accountCreationDate;
}
