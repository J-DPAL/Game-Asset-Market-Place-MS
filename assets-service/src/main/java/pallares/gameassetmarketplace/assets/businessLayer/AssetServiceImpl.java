package pallares.gameassetmarketplace.assets.businessLayer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pallares.gameassetmarketplace.assets.presentationLayer.AssetRequestModel;
import pallares.gameassetmarketplace.assets.presentationLayer.AssetResponseModel;
import pallares.gameassetmarketplace.assets.dataAccessLayer.*;
import pallares.gameassetmarketplace.assets.utils.exceptions.AssetFileMissingException;
import pallares.gameassetmarketplace.assets.utils.exceptions.NotFoundException;
import pallares.gameassetmarketplace.assets.mappingLayer.AssetRequestMapper;
import pallares.gameassetmarketplace.assets.mappingLayer.AssetResponseMapper;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final AssetResponseMapper assetResponseMapper;
    private final AssetRequestMapper assetRequestMapper;

    public AssetServiceImpl(AssetRepository assetRepository, AssetResponseMapper assetResponseMapper, AssetRequestMapper assetRequestMapper) {
        this.assetRepository = assetRepository;
        this.assetResponseMapper = assetResponseMapper;
        this.assetRequestMapper = assetRequestMapper;
    }

    @Override
    public List<AssetResponseModel> getAllAssets() {
        List<Asset> assets = assetRepository.findAll();
        return assetResponseMapper.entityListToResponseModelList(assets);
    }

    @Override
    public AssetResponseModel addAsset(AssetRequestModel assetRequestModel) {
        Asset asset = assetRequestMapper.requestModelToEntity(assetRequestModel, new AssetIdentifier());
        if (asset.getFileUrl() == null || asset.getFileUrl().isBlank()) {
            throw new AssetFileMissingException("File URL cannot be null or blank.");
        }
        return assetResponseMapper.entityToResponseModel(assetRepository.save(asset));
    }

    @Override
    public AssetResponseModel updateAsset(AssetRequestModel assetRequestModel, String assetId) {
        Asset existingAsset = assetRepository.findByAssetIdentifier_AssetId(assetId);

        if (existingAsset == null) {
            throw new NotFoundException("Provided assetIdentifier not found: " + assetId);
        }

        if (existingAsset.getFileUrl() == null || existingAsset.getFileUrl().isBlank()) {
            throw new AssetFileMissingException("File URL cannot be null or blank.");
        }

        Asset updatedAsset = assetRequestMapper.requestModelToEntity(assetRequestModel, existingAsset.getAssetIdentifier());
        updatedAsset.setId(existingAsset.getId());

        Asset response = assetRepository.save(updatedAsset);
        return assetResponseMapper.entityToResponseModel(response);
    }

    @Override
    public void removeAsset(String assetId) {
        Asset existingAsset = assetRepository.findByAssetIdentifier_AssetId(assetId);

        if (existingAsset == null) {
            throw new NotFoundException("Provided assetIdentifier not found: " + assetId);
        }

        assetRepository.delete(existingAsset);
    }

    @Override
    public AssetResponseModel getAssetByAssetId(String assetId) {
        Asset asset = assetRepository.findByAssetIdentifier_AssetId(assetId);

        if (asset == null) {
            throw new NotFoundException("Provided assetIdentifier not found: " + assetId);
        }
        return assetResponseMapper.entityToResponseModel(asset);
    }
}

