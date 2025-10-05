package pallares.gameassetmarketplace.assets.mappingLayer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pallares.gameassetmarketplace.assets.dataAccessLayer.Asset;
import pallares.gameassetmarketplace.assets.dataAccessLayer.AssetIdentifier;
import pallares.gameassetmarketplace.assets.dataAccessLayer.AssetType;
import pallares.gameassetmarketplace.assets.dataAccessLayer.LicenseType;
import pallares.gameassetmarketplace.assets.presentationLayer.AssetRequestModel;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface AssetRequestMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "assetIdentifier", source = "assetIdentifier"),
            @Mapping(target = "name", source = "assetRequestModel.name"),
            @Mapping(target = "description", source = "assetRequestModel.description"),
            @Mapping(target = "assetType", source = "assetRequestModel.assetType"),
            @Mapping(target = "price", source = "assetRequestModel.price"),
            @Mapping(target = "fileUrl", source = "assetRequestModel.fileUrl"),
            @Mapping(target = "thumbnailUrl", source = "assetRequestModel.thumbnailUrl"),
            @Mapping(target = "licenseType", source = "assetRequestModel.licenseType"),
            @Mapping(target = "createdDate", source = "assetRequestModel.createdDate"),
            @Mapping(target = "updatedDate", source = "assetRequestModel.updatedDate")
    })
    Asset requestModelToEntity(AssetRequestModel assetRequestModel, AssetIdentifier assetIdentifier);
}
