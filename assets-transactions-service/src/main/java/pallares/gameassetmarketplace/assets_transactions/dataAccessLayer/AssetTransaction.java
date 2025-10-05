package pallares.gameassetmarketplace.assets_transactions.dataAccessLayer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.AssetModel;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments.PaymentModel;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users.UserModel;

@Document(collection = "asset_transactions")
@Data
@Builder
@AllArgsConstructor
public class AssetTransaction {

    @Id
    private String id; // Private identifier

    private AssetTransactionIdentifier assetTransactionIdentifier; // Public identifier

    private UserModel userModel;

    private PaymentModel paymentModel;

    private AssetModel assetModel;

    private AssetTransactionStatus status;

    private AssetTransactionType type;

    private Discount discount;

    public AssetTransaction (UserModel userModel, PaymentModel paymentModel, AssetModel assetModel, AssetTransactionStatus status, AssetTransactionType type, Discount discount) {
        this.assetTransactionIdentifier = new AssetTransactionIdentifier();
        this.userModel = userModel;
        this.paymentModel = paymentModel;
        this.assetModel = assetModel;
        this.status = status;
        this.type = type;
        this.discount = discount;
    }

    public AssetTransaction() {

    }
}
