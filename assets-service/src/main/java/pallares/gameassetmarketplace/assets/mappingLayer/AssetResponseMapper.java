package pallares.gameassetmarketplace.assets.mappingLayer;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;
import pallares.gameassetmarketplace.assets.dataAccessLayer.Asset;
import pallares.gameassetmarketplace.assets.presentationLayer.AssetController;
import pallares.gameassetmarketplace.assets.presentationLayer.AssetResponseModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper(componentModel = "spring")
public interface AssetResponseMapper {

    @Mapping(expression = "java(asset.getAssetIdentifier().getAssetId())", target = "assetId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "assetType", target = "assetType")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "fileUrl", target = "fileUrl")
    @Mapping(source = "thumbnailUrl", target = "thumbnailUrl")
    @Mapping(source = "licenseType", target = "licenseType")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "updatedDate", target = "updatedDate")
    AssetResponseModel entityToResponseModel(Asset asset);

    List<AssetResponseModel> entityListToResponseModelList(List<Asset> assets);

    @AfterMapping
    default void addLinks(@MappingTarget AssetResponseModel assetResponseModel, Asset asset) {
        // Self Link
        Link selfLink = linkTo(methodOn(AssetController.class)
                .getAssetByAssetId(assetResponseModel.getAssetId()))
                .withSelfRel();
        assetResponseModel.add(selfLink);

        // Link to all assets
        Link allAssetsLink = linkTo(methodOn(AssetController.class)
                .getAllAssets())
                .withRel("assets");
        assetResponseModel.add(allAssetsLink);
    }
}
