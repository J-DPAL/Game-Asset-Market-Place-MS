package pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions.AssetTransactionStatus;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions.AssetTransactionType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions.Discount;

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
