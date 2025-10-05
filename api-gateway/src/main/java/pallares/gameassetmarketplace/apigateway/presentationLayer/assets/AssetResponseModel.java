package pallares.gameassetmarketplace.apigateway.presentationLayer.assets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assets.AssetType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assets.LicenseType;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
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
