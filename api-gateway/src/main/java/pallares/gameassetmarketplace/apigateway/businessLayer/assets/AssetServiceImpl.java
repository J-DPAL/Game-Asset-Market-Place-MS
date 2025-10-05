package pallares.gameassetmarketplace.apigateway.businessLayer.assets;

import pallares.gameassetmarketplace.apigateway.domainclientLayer.assets.AssetsServiceClient;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assets.AssetRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assets.AssetResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AssetServiceImpl implements AssetService {

    private final AssetsServiceClient assetsServiceClient;

    @Autowired
    public AssetServiceImpl(AssetsServiceClient assetsServiceClient) {
        this.assetsServiceClient = assetsServiceClient;
    }

    @Override
    public AssetResponseModel getAssetByAssetId(String assetId) {
        log.debug("Fetching asset with ID: {}", assetId);
        return assetsServiceClient.getAssetByAssetId(assetId);
    }

    @Override
    public List<AssetResponseModel> getAllAssets() {
        log.debug("Fetching all assets");
        return assetsServiceClient.getAssets();
    }

    @Override
    public AssetResponseModel addAsset(AssetRequestModel assetRequestModel) {
        log.debug("Creating new asset: {}", assetRequestModel);
        return assetsServiceClient.addAsset(assetRequestModel);
    }

    @Override
    public AssetResponseModel updateAsset(AssetRequestModel assetRequestModel, String assetId) {
        log.debug("Updating asset with ID: {}", assetId);
        assetsServiceClient.updateAsset(assetId, assetRequestModel);
        return assetsServiceClient.getAssetByAssetId(assetId);
    }

    @Override
    public void removeAsset(String assetId) {
        log.debug("Deleting asset with ID: {}", assetId);
        assetsServiceClient.removeAsset(assetId);
    }
}
