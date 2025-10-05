package pallares.gameassetmarketplace.apigateway.presentationLayer.users;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.users.PhoneNumber;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.users.UserRoleEnum;

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
