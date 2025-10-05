package pallares.gameassetmarketplace.apigateway.presentationLayer.payments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.Currency;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.PaymentType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.TransactionStatus;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseModel extends RepresentationModel<PaymentResponseModel> {
    String paymentId;
    BigDecimal price;
    Currency currency;
    PaymentType paymentType;
    TransactionStatus transactionStatus;
}
