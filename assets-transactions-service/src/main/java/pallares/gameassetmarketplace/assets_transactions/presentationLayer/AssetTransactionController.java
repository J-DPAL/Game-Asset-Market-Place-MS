package pallares.gameassetmarketplace.assets_transactions.presentationLayer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pallares.gameassetmarketplace.assets_transactions.businessLayer.AssetTransactionService;
import pallares.gameassetmarketplace.assets_transactions.utils.exceptions.InvalidInputException;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v1/users/{userId}/asset_transactions")
public class AssetTransactionController {

    private final AssetTransactionService assetTransactionService;
    private static final int UUID_LENGTH = 36;

    public AssetTransactionController(AssetTransactionService assetTransactionService) {
        this.assetTransactionService = assetTransactionService;
    }

    @GetMapping()
    public ResponseEntity<List<AssetTransactionResponseModel>> getAllAssetTransactions(@PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userIdentifier provided: " + userId);
        }
        return ResponseEntity.ok().body(assetTransactionService.getAllAssetTransactions(userId));
    }

    @GetMapping("/{assetTransactionId}")
    public ResponseEntity<AssetTransactionResponseModel> getAssetTransactionByAssetTransactionId(@PathVariable String assetTransactionId, @PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userIdentifier provided: " + userId);
        }
        if (assetTransactionId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid assetTransactionIdentifier provided: " + assetTransactionId);
        }
        return ResponseEntity.ok().body(assetTransactionService.getAssetTransactionByAssetTransactionId(assetTransactionId, userId));
    }

    @PostMapping()
    public ResponseEntity<AssetTransactionResponseModel> addAssetTransaction(@RequestBody AssetTransactionRequestModel assetTransactionRequestModel, @PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userIdentifier provided: " + userId);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(assetTransactionService.addAssetTransaction(assetTransactionRequestModel, userId));
    }

    @PutMapping("/{assetTransactionId}")
    public ResponseEntity<AssetTransactionResponseModel> updateAssetTransaction(@RequestBody AssetTransactionRequestModel assetTransactionRequestModel, @PathVariable String assetTransactionId, @PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userIdentifier provided: " + userId);
        }
        if (assetTransactionId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid assetTransactionIdentifier provided: " + assetTransactionId);
        }
        return ResponseEntity.ok().body(assetTransactionService.updateAssetTransaction(assetTransactionRequestModel, assetTransactionId, userId));
    }

    @DeleteMapping("/{assetTransactionId}")
    public ResponseEntity<Void> removeAssetTransaction(@PathVariable String assetTransactionId, @PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userIdentifier provided: " + userId);
        }
        if (assetTransactionId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid assetTransactionIdentifier provided: " + assetTransactionId);
        }
        assetTransactionService.removeAssetTransaction(assetTransactionId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
