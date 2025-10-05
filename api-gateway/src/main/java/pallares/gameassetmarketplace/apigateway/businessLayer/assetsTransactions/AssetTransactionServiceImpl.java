package pallares.gameassetmarketplace.apigateway.businessLayer.assetsTransactions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions.AssetTransactionServiceClient;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions.AssetTransactionRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions.AssetTransactionResponseModel;

import java.util.List;

@Slf4j
@Service
public class AssetTransactionServiceImpl implements AssetTransactionService {

    private final AssetTransactionServiceClient transactionClient;

    @Autowired
    public AssetTransactionServiceImpl(AssetTransactionServiceClient transactionClient) {
        this.transactionClient = transactionClient;
    }

    @Override
    public List<AssetTransactionResponseModel> getAllAssetTransactions(String userId) {
        log.debug("Fetching all asset transactions for userId={}", userId);
        return transactionClient.getAllAssetTransactions(userId);
    }

    @Override
    public AssetTransactionResponseModel getAssetTransactionByAssetTransactionId(String txId, String userId) {
        log.debug("Fetching asset transaction txId={} for userId={}", txId, userId);
        return transactionClient.getAssetTransactionById(txId, userId);
    }

    @Override
    public AssetTransactionResponseModel addAssetTransaction(AssetTransactionRequestModel requestModel, String userId) {
        log.debug("Creating new asset transaction for userId={} -> {}", requestModel, userId);
        return transactionClient.addAssetTransaction(requestModel, userId);
    }

    @Override
    public AssetTransactionResponseModel updateAssetTransaction(AssetTransactionRequestModel requestModel, String txId, String userId) {
        log.debug("Updating asset transaction txId={} for userId={} -> {}", requestModel, txId, userId);
        transactionClient.updateAssetTransaction(requestModel, txId, userId);
        // retrieve updated resource
        return transactionClient.getAssetTransactionById(txId, userId);
    }

    @Override
    public void removeAssetTransaction(String txId, String userId) {
        log.debug("Deleting asset transaction txId={} for userId={}", txId, userId);
        transactionClient.removeAssetTransaction(txId, userId);
    }
}
