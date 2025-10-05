package pallares.gameassetmarketplace.users.dataAccessLayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Objects;


@Embeddable
@Getter
public class EmailAddress {

    private String email;

    public EmailAddress() {
    }

    public EmailAddress(@NotNull String email) {

        Objects.requireNonNull(this.email = email);
    }

}
