package pallares.gameassetmarketplace.payments.dataAccessLayer;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name="payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //private identifier

    @Embedded
    private PaymentIdentifier paymentIdentifier; //public identifier

    @Embedded
    private ItemPrice itemPrice;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    public Payment(@NotNull ItemPrice itemPrice, @NotNull PaymentType paymentType, @NotNull TransactionStatus transactionStatus) {
        this.paymentIdentifier = new PaymentIdentifier();
        this.itemPrice = itemPrice;
        this.paymentType = paymentType;
        this.transactionStatus = transactionStatus;
    }

    public Payment() {
    }
}
