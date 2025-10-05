package pallares.gameassetmarketplace.assets_transactions.businessLayer;

import org.springframework.stereotype.Service;
import pallares.gameassetmarketplace.assets_transactions.presentationLayer.AssetTransactionRequestModel;
import pallares.gameassetmarketplace.assets_transactions.presentationLayer.AssetTransactionResponseModel;

import java.util.List;
import java.util.Map;

@Service
public interface AssetTransactionService {

    List<AssetTransactionResponseModel> getAllAssetTransactions(String userId);
    AssetTransactionResponseModel getAssetTransactionByAssetTransactionId(String assetTransactionId, String userId);
    AssetTransactionResponseModel addAssetTransaction(AssetTransactionRequestModel assetTransactionRequestModel, String userId);
    AssetTransactionResponseModel updateAssetTransaction(AssetTransactionRequestModel assetTransactionRequestModel, String assetTransactionId, String userId);
    void removeAssetTransaction(String assetTransactionId, String userId);
}
