package pallares.gameassetmarketplace.assets_transactions.dataAccessLayer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetTransactionRepository extends MongoRepository<AssetTransaction, String> {
    AssetTransaction findByAssetTransactionIdentifier_AssetTransactionId(String assetTransactionId);
    AssetTransaction findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(String assetTransactionId, String userId);
    boolean existsByUserModel_UserIdAndAssetModel_AssetId(String userId, String assetId);
    List<AssetTransaction> findAllByUserModel_UserId(String userId);
}
