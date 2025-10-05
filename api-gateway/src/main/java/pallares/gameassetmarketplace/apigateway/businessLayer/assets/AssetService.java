package pallares.gameassetmarketplace.apigateway.businessLayer.assets;

import org.springframework.stereotype.Service;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assets.AssetRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assets.AssetResponseModel;

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
