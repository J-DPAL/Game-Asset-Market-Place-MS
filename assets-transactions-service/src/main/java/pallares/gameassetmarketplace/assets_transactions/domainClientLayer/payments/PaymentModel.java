package pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentModel {
    private String paymentId;

    // these two match the JSON directly…
    private BigDecimal price;
    private Currency currency;

    private PaymentType paymentType;
    private TransactionStatus transactionStatus;

    public ItemPrice getItemPrice() {
        return new ItemPrice(price, currency);
    }
}

