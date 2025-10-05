package pallares.gameassetmarketplace.payments.dataAccessLayer;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;

@Embeddable
@Getter
public class ItemPrice {
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public ItemPrice() {

    }
    public ItemPrice(@NotNull BigDecimal price, @NotNull Currency currency) {
        this.price = price;
        this.currency = currency;
    }
}
