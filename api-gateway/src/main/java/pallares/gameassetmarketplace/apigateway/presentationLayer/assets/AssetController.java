package pallares.gameassetmarketplace.apigateway.presentationLayer.assets;

import pallares.gameassetmarketplace.apigateway.businessLayer.assets.AssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    // GET /api/v1/assets/{assetId}
    @GetMapping(value = "/{assetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AssetResponseModel> getAssetByAssetId(@PathVariable String assetId) {
        log.debug("1. Request Received in API-Gateway Assets Controller: getAssetByAssetId");
        return ResponseEntity.ok(assetService.getAssetByAssetId(assetId));
    }

    // GET /api/v1/assets
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AssetResponseModel>> getAllAssets() {
        log.debug("2. Request Received in API-Gateway Assets Controller: getAllAssets");
        return ResponseEntity.ok(assetService.getAllAssets());
    }

    // POST /api/v1/assets
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AssetResponseModel> addAsset(@RequestBody AssetRequestModel assetRequestModel) {
        log.debug("3. Request Received in API-Gateway Assets Controller: addAsset");
        return ResponseEntity.status(HttpStatus.CREATED).body(assetService.addAsset(assetRequestModel));
    }

    // PUT /api/v1/assets/{assetId}
    @PutMapping(value = "/{assetId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AssetResponseModel> updateAsset(@PathVariable String assetId,
                                                                  @RequestBody AssetRequestModel assetRequestModel) {
        log.debug("4. Request Received in API-Gateway Assets Controller: updateAsset");
        return ResponseEntity.ok(assetService.updateAsset(assetRequestModel, assetId));
    }

    // DELETE /api/v1/assets/{assetId}
    @DeleteMapping(value = "/{assetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AssetResponseModel> removeAsset(@PathVariable String assetId) {
        log.debug("5. Request Received in API-Gateway Assets Controller: removeAsset");
        assetService.removeAsset(assetId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
