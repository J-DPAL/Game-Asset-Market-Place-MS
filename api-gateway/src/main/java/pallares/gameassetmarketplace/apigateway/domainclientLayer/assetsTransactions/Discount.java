package pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class Discount {
    private BigDecimal percentage;
    private BigDecimal amountOff;
    private String reason;

    public Discount(BigDecimal percentage, BigDecimal amountOff, String reason) {
        this.percentage = percentage;
        this.amountOff = amountOff;
        this.reason = reason;
    }
}
