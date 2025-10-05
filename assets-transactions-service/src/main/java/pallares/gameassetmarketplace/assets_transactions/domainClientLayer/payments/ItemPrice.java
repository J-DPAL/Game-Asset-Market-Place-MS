package pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemPrice {
    private BigDecimal price;
    private Currency currency;

    public ItemPrice() {

    }
    public ItemPrice(BigDecimal price, Currency currency) {
        this.price = price;
        this.currency = currency;
    }
}
