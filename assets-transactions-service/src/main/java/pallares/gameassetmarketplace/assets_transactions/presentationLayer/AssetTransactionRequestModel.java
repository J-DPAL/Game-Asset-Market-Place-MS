package pallares.gameassetmarketplace.assets_transactions.presentationLayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pallares.gameassetmarketplace.assets_transactions.dataAccessLayer.AssetTransactionStatus;
import pallares.gameassetmarketplace.assets_transactions.dataAccessLayer.AssetTransactionType;
import pallares.gameassetmarketplace.assets_transactions.dataAccessLayer.Discount;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetTransactionRequestModel {
    String userId;
    String paymentId;
    String assetId;
    AssetTransactionStatus status;
    AssetTransactionType type;
    Discount discount;
}
