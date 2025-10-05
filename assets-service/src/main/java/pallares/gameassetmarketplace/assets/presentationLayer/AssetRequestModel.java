package pallares.gameassetmarketplace.assets.presentationLayer;

import lombok.*;
import pallares.gameassetmarketplace.assets.dataAccessLayer.AssetType;
import pallares.gameassetmarketplace.assets.dataAccessLayer.LicenseType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
