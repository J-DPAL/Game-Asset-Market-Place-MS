package pallares.gameassetmarketplace.payments.presentationLayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pallares.gameassetmarketplace.payments.dataAccessLayer.Currency;
import pallares.gameassetmarketplace.payments.dataAccessLayer.PaymentType;
import pallares.gameassetmarketplace.payments.dataAccessLayer.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

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
