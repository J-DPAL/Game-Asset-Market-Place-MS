package pallares.gameassetmarketplace.assets.dataAccessLayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Integer> {
    Asset findByAssetIdentifier_AssetId(String assetId);
}
