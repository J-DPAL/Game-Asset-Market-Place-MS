package pallares.gameassetmarketplace.assets_transactions.presentationLayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pallares.gameassetmarketplace.assets_transactions.dataAccessLayer.AssetTransactionStatus;
import pallares.gameassetmarketplace.assets_transactions.dataAccessLayer.AssetTransactionType;
import pallares.gameassetmarketplace.assets_transactions.dataAccessLayer.Discount;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.AssetType;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.LicenseType;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments.Currency;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments.PaymentType;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments.TransactionStatus;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users.EmailAddress;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users.PhoneNumber;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetTransactionResponseModel extends RepresentationModel<AssetTransactionResponseModel> {
    String assetTransactionId;
    String userId;
    String username; // get from User Service
    EmailAddress emailAddress;
    PhoneNumber phoneNumber;
    String paymentId;
    BigDecimal price; // get from Payment Service
    Currency currency; // get from Payment Service
    PaymentType paymentType; // get from Payment Service
    TransactionStatus transactionStatus;
    String assetId;
    String name; // get from Asset Service
    String description; // get from Asset Service
    AssetType assetType; // get from Asset Service
    String fileUrl;
    String thumbnailUrl;
    LicenseType licenseType;
    AssetTransactionStatus status;
    AssetTransactionType type;
    Discount discount;
}
