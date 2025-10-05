package pallares.gameassetmarketplace.payments.mappingLayer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pallares.gameassetmarketplace.payments.dataAccessLayer.Payment;
import pallares.gameassetmarketplace.payments.dataAccessLayer.PaymentIdentifier;
import pallares.gameassetmarketplace.payments.presentationLayer.PaymentRequestModel;
import pallares.gameassetmarketplace.payments.dataAccessLayer.ItemPrice;
import pallares.gameassetmarketplace.payments.dataAccessLayer.Currency;
import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface PaymentRequestMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "paymentIdentifier", source = "paymentIdentifier"),
            @Mapping(target = "itemPrice", expression = "java(mapToItemPrice(paymentRequestModel.getPrice(), paymentRequestModel.getCurrency()))"),
            @Mapping(source = "paymentRequestModel.paymentType", target = "paymentType"),
            @Mapping(source = "paymentRequestModel.transactionStatus", target = "transactionStatus")
    })
    Payment requestModelToEntity(PaymentRequestModel paymentRequestModel, PaymentIdentifier paymentIdentifier);

    // Helper method for mapping itemPrice
    default ItemPrice mapToItemPrice(BigDecimal price, Currency currency) {
        return new ItemPrice(price, currency); // Ensure this constructor exists
    }
}
