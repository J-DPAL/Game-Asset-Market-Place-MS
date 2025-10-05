package pallares.gameassetmarketplace.users.dataAccessLayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class UserIdentifier {

    private String userId;

    public UserIdentifier() {
        this.userId = UUID.randomUUID().toString();
    }

    public UserIdentifier(String userId) {
        this.userId = userId;
    }
}
