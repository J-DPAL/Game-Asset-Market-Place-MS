package pallares.gameassetmarketplace.apigateway.presentationLayer.payments;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.Currency;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.PaymentType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.TransactionStatus;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestModel {
    BigDecimal price;
    Currency currency;
    PaymentType paymentType;
    TransactionStatus transactionStatus;
}
