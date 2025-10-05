package pallares.gameassetmarketplace.assets_transactions.dataAccessLayer;

import lombok.Getter;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.UUID;

@Getter
public class AssetTransactionIdentifier {

    @Indexed(unique = true)
    private String assetTransactionId;

    public AssetTransactionIdentifier() {
        this.assetTransactionId = UUID.randomUUID().toString();
    }

    public AssetTransactionIdentifier(String assetTransactionId) {
        this.assetTransactionId = assetTransactionId;
    }
}
