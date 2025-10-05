package pallares.gameassetmarketplace.apigateway.domainclientLayer.users;


import jakarta.persistence.Embeddable;
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
