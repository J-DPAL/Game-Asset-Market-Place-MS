package pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import pallares.gameassetmarketplace.apigateway.domainclientLayer.assets.AssetType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assets.LicenseType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions.AssetTransactionStatus;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions.AssetTransactionType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions.Discount;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.Currency;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.PaymentType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.payments.TransactionStatus;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.users.EmailAddress;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.users.PhoneNumber;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetTransactionResponseModel extends RepresentationModel<AssetTransactionResponseModel> {
    String assetTransactionId;
    String userId;
    String username;
    EmailAddress emailAddress;
    PhoneNumber phoneNumber;
    String paymentId;
    BigDecimal price;
    Currency currency;
    PaymentType paymentType;
    TransactionStatus transactionStatus;
    String assetId;
    String name;
    String description;
    AssetType assetType;
    String fileUrl;
    String thumbnailUrl;
    LicenseType licenseType;
    AssetTransactionStatus status;
    AssetTransactionType type;
    Discount discount;
}
