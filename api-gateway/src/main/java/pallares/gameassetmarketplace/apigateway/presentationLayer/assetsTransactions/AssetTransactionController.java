package pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pallares.gameassetmarketplace.apigateway.businessLayer.assetsTransactions.AssetTransactionService;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/users/{userId}/asset_transactions")
public class AssetTransactionController {

    private final AssetTransactionService assetTransactionService;

    private final AssetTransactionModelAssembler assembler;

    public AssetTransactionController(AssetTransactionService assetTransactionService,
                                      AssetTransactionModelAssembler assembler) {
        this.assetTransactionService = assetTransactionService;
        this.assembler = assembler;
    }


    // GET /api/v1/users/{userId}/asset_transactions
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AssetTransactionResponseModel>> getAll(
            @PathVariable String userId) {

        log.debug("1. Request Received in API-Gateway AssetTransaction Controller: getAllAssetTransactions for userId={}", userId);
        List<AssetTransactionResponseModel> list = assetTransactionService.getAllAssetTransactions(userId);
        return ResponseEntity.ok(list);
    }

    // GET /api/v1/users/{userId}/asset_transactions/{txId}
    @GetMapping(value = "/{txId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AssetTransactionResponseModel> getById(
            @PathVariable String userId,
            @PathVariable String txId) {

        log.debug("2. Request Received in API-Gateway AssetTransaction Controller: getAssetTransactionById userId={}", userId);
        AssetTransactionResponseModel model = assetTransactionService.getAssetTransactionByAssetTransactionId(txId, userId);
        return ResponseEntity.ok(model);
    }

    // POST /api/v1/users/{userId}/asset_transactions
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AssetTransactionResponseModel> create(
            @PathVariable String userId,
            @RequestBody AssetTransactionRequestModel requestModel) {

        log.debug("3. Request Received in API-Gateway AssetTransaction Controller: addAssetTransaction for userId={}", userId);
        AssetTransactionResponseModel created = assetTransactionService.addAssetTransaction(requestModel, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/v1/users/{userId}/asset_transactions/{txId}
    @PutMapping(value = "/{txId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AssetTransactionResponseModel> update(
            @PathVariable String userId,
            @PathVariable String txId,
            @RequestBody AssetTransactionRequestModel requestModel) {

        log.debug("4. Request Received in API-Gateway AssetTransaction Controller: updateAssetTransaction txId={} for userId={}", txId, userId);
        AssetTransactionResponseModel updated = assetTransactionService.updateAssetTransaction(requestModel, txId, userId);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/v1/users/{userId}/asset_transactions/{txId}
    @DeleteMapping(value = "/{txId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(
            @PathVariable String userId,
            @PathVariable String txId) {

        log.debug("5. Request Received in API-Gateway AssetTransaction Controller: removeAssetTransaction txId={} for userId={}", txId, userId);
        assetTransactionService.removeAssetTransaction(txId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
