package pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class UserModel {
    String userId;
    String username;
    EmailAddress emailAddress;
    PhoneNumber phoneNumber;
}
