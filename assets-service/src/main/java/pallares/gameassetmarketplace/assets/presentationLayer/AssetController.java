package pallares.gameassetmarketplace.assets.presentationLayer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pallares.gameassetmarketplace.assets.businessLayer.AssetService;
import pallares.gameassetmarketplace.assets.utils.exceptions.InvalidInputException;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v1/assets")
public class AssetController {

    private final AssetService assetService;
    private static final int UUID_LENGTH = 36;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping()
    public ResponseEntity<List<AssetResponseModel>> getAllAssets() {
        return ResponseEntity.ok().body(assetService.getAllAssets());
    }

    @GetMapping("/{assetId}")
    public ResponseEntity<AssetResponseModel> getAssetByAssetId(@PathVariable String assetId) {
        if (assetId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid assetIdentifier provided: " + assetId);
        }
        return ResponseEntity.ok().body(assetService.getAssetByAssetId(assetId));
    }

    @PostMapping()
    public ResponseEntity<AssetResponseModel> addAsset(@RequestBody AssetRequestModel assetRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(assetService.addAsset(assetRequestModel));
    }

    @PutMapping("/{assetId}")
    public ResponseEntity<AssetResponseModel> updateAsset(@RequestBody AssetRequestModel assetRequestModel, @PathVariable String assetId, Map<String, String> queryParams) {
        if (assetId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid assetIdentifier provided: " + assetId);
        }
        return ResponseEntity.ok().body(assetService.updateAsset(assetRequestModel, assetId));
    }

    @DeleteMapping("/{assetId}")
    public ResponseEntity<Void> deleteAsset(@PathVariable String assetId) {
        if (assetId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid assetIdentifier provided: " + assetId);
        }
        assetService.removeAsset(assetId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
