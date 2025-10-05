package pallares.gameassetmarketplace.apigateway.businessLayer.assetsTransactions;

import org.springframework.stereotype.Service;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions.AssetTransactionRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions.AssetTransactionResponseModel;

import java.util.List;

@Service
public interface AssetTransactionService {

    List<AssetTransactionResponseModel> getAllAssetTransactions(String userId);
    AssetTransactionResponseModel getAssetTransactionByAssetTransactionId(String assetTransactionId, String userId);
    AssetTransactionResponseModel addAssetTransaction(AssetTransactionRequestModel assetTransactionRequestModel, String userId);
    AssetTransactionResponseModel updateAssetTransaction(AssetTransactionRequestModel assetTransactionRequestModel, String assetTransactionId, String userId);
    void removeAssetTransaction(String assetTransactionId, String userId);
}
