package pallares.gameassetmarketplace.users.presentationLayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pallares.gameassetmarketplace.users.dataAccessLayer.UserRoleEnum;
import pallares.gameassetmarketplace.users.dataAccessLayer.PhoneNumber;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseModel extends RepresentationModel<UserResponseModel> {
    String userId;
    String firstName;
    String lastName;
    String username;
    String emailAddress;
    UserRoleEnum userRoleEnum;
    PhoneNumber phoneNumber;
    LocalDate accountCreationDate;
}
