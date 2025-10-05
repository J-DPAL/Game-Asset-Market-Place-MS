package pallares.gameassetmarketplace.assets.presentationLayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pallares.gameassetmarketplace.assets.dataAccessLayer.AssetType;
import pallares.gameassetmarketplace.assets.dataAccessLayer.LicenseType;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetResponseModel extends RepresentationModel<AssetResponseModel> {
    String assetId;
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
