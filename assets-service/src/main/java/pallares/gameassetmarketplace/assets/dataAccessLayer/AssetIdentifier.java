package pallares.gameassetmarketplace.assets.dataAccessLayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class AssetIdentifier {

    private String assetId;

    public AssetIdentifier() {
        this.assetId = UUID.randomUUID().toString();
    }

    public AssetIdentifier(String assetId) {
        this.assetId = assetId;
    }
}
