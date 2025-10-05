package pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class AssetModel {
    String assetId;
    String name;
    String description;
    AssetType assetType;
    String fileUrl;
    String thumbnailUrl;
    LicenseType licenseType;
}
