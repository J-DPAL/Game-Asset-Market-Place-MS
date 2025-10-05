package pallares.gameassetmarketplace.users.dataAccessLayer;


import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;


@Embeddable
@NoArgsConstructor
@Getter
public class PhoneNumber {
    private String number;

        public PhoneNumber(@NotNull String number) {
        this.number = number;
    }
}
