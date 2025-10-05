package pallares.gameassetmarketplace.users.presentationLayer;


import lombok.*;
import pallares.gameassetmarketplace.users.dataAccessLayer.UserRoleEnum;
import pallares.gameassetmarketplace.users.dataAccessLayer.PhoneNumber;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestModel {
    String firstName;
    String lastName;
    String username;
    String emailAddress;
    UserRoleEnum userRoleEnum;
    PhoneNumber phoneNumber;
    LocalDate accountCreationDate;
}
