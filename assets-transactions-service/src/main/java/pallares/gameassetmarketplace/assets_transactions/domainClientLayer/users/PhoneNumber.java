package pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PhoneNumber {
    private String number;
        public PhoneNumber(String number) {
            this.number = number;
    }
}
