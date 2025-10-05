package pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users;

import lombok.Getter;
import java.util.Objects;

@Getter
public class EmailAddress {

    private String email;

    public EmailAddress() {
    }

    public EmailAddress(String email) {
        Objects.requireNonNull(this.email = email);
    }

}
