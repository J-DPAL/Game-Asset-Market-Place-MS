package pallares.gameassetmarketplace.assets.businessLayer;

import org.springframework.stereotype.Service;
import pallares.gameassetmarketplace.assets.presentationLayer.AssetResponseModel;
import pallares.gameassetmarketplace.assets.presentationLayer.AssetRequestModel;
import java.util.List;
import java.util.Map;

@Service
public interface AssetService {

    List<AssetResponseModel> getAllAssets();
    AssetResponseModel getAssetByAssetId(String assetId);
    AssetResponseModel addAsset(AssetRequestModel assetRequestModel);
    AssetResponseModel updateAsset(AssetRequestModel assetRequestModel, String assetId);
    void removeAsset(String assetId);
}
