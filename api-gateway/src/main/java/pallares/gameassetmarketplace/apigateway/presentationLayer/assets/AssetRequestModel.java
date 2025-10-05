package pallares.gameassetmarketplace.apigateway.presentationLayer.assets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assets.AssetType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assets.LicenseType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetRequestModel {
    String name;
    String description;
    AssetType assetType;
    BigDecimal price;
    String fileUrl;
    String thumbnailUrl;
    LicenseType licenseType;
    LocalDate createdDate;
    LocalDate updatedDate;
}
