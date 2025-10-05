package pallares.gameassetmarketplace.payments.presentationLayer;


import lombok.*;
import pallares.gameassetmarketplace.payments.dataAccessLayer.Currency;
import pallares.gameassetmarketplace.payments.dataAccessLayer.PaymentType;
import pallares.gameassetmarketplace.payments.dataAccessLayer.TransactionStatus;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestModel {
    BigDecimal price;
    Currency currency;
    PaymentType paymentType;
    TransactionStatus transactionStatus;
}
