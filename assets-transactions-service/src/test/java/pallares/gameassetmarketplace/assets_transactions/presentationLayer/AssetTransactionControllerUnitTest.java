package pallares.gameassetmarketplace.assets_transactions.presentationLayer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pallares.gameassetmarketplace.assets_transactions.businessLayer.AssetTransactionService;
import pallares.gameassetmarketplace.assets_transactions.utils.exceptions.InvalidInputException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles("test")
class AssetTransactionControllerUnitTest {
    @Autowired
    AssetTransactionController assetTransactionController;

    @MockitoBean
    AssetTransactionService assetTransactionService;

    private final String BASE_URI = "/api/v1/asset_transactions";
    private final String FOUND_ASSET_TRANSACTION_ID = "a1b2c3d4-e5f6-7890-1234-56789abcdef0";
    private final String NOT_FOUND_ASSET_TRANSACTION_ID = "c9040au79-cb47-6d96-888e-ff96908db4d8";
    private final String INVALID_ASSET_TRANSACTION_ID = "a1b2c3d4-e5f6-7890-1234-56789abcde";
    private final String FOUND_USER_ID = "c5440a89-cb47-4d96-888e-yy96708db4d9";
    private final String BASE_URI_ASSETS = "http://localhost:7001/api/v1/assets";
    private final String BASE_URI_PAYMENTS = "http://localhost:7002/api/v1/payments";
    private final String BASE_URI_USERS = "http://localhost:7003/api/v1/users";

    @Test
    void whenNoAssetTransactionsExists_thenReturnEmptyList() {
        when(assetTransactionService.getAllAssetTransactions(FOUND_USER_ID))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<AssetTransactionResponseModel>> assetTransactionResponseEntity = assetTransactionController.getAllAssetTransactions(FOUND_USER_ID);

        assertEquals(HttpStatus.OK, assetTransactionResponseEntity.getStatusCode());
        assertTrue(assetTransactionResponseEntity.getBody().isEmpty());
    }

    @Test
    void whenAssetTransactionIdInvalid_thenThrowInvalidInputException() {
        assertThrowsExactly(InvalidInputException.class, () ->
                assetTransactionController.getAssetTransactionByAssetTransactionId(INVALID_ASSET_TRANSACTION_ID, FOUND_USER_ID)
        );
        verify(assetTransactionService, never()).getAssetTransactionByAssetTransactionId(any(), any());
    }

    @Test
    void whenAssetTransactionFound_thenReturnOK() {
        var model = new AssetTransactionResponseModel();
        model.setAssetTransactionId(FOUND_ASSET_TRANSACTION_ID);
        when(assetTransactionService.getAssetTransactionByAssetTransactionId(FOUND_ASSET_TRANSACTION_ID, FOUND_USER_ID))
                .thenReturn(model);

        var assetTransactionResponseEntity = assetTransactionController.getAssetTransactionByAssetTransactionId(FOUND_ASSET_TRANSACTION_ID, FOUND_USER_ID);
        assertEquals(HttpStatus.OK, assetTransactionResponseEntity.getStatusCode());
        assertEquals(FOUND_ASSET_TRANSACTION_ID, assetTransactionResponseEntity.getBody().getAssetTransactionId());
        verify(assetTransactionService).getAssetTransactionByAssetTransactionId(FOUND_ASSET_TRANSACTION_ID, FOUND_USER_ID);
    }

    @Test
    void whenAddAssetTransaction_thenReturnCreated() {
        var assetTransactionRequestModel  = new AssetTransactionRequestModel();
        var assetTransactionResponseEntityModel = new AssetTransactionResponseModel();
        assetTransactionResponseEntityModel.setAssetTransactionId(FOUND_ASSET_TRANSACTION_ID);

        when(assetTransactionService.addAssetTransaction(assetTransactionRequestModel, FOUND_USER_ID))
                .thenReturn(assetTransactionResponseEntityModel);

        var assetTransactionResponseEntity = assetTransactionController.addAssetTransaction(assetTransactionRequestModel, FOUND_USER_ID);
        assertEquals(HttpStatus.CREATED, assetTransactionResponseEntity.getStatusCode());
        assertEquals(FOUND_ASSET_TRANSACTION_ID, assetTransactionResponseEntity.getBody().getAssetTransactionId());
        verify(assetTransactionService).addAssetTransaction(assetTransactionRequestModel, FOUND_USER_ID);
    }

    @Test
    void whenUpdateAssetTransaction_thenReturnOK() {
        var assetTransactionRequestModel = new AssetTransactionRequestModel();
        var assetTransactionResponseEntityModel = new AssetTransactionResponseModel();
        assetTransactionResponseEntityModel.setAssetTransactionId(FOUND_ASSET_TRANSACTION_ID);

        when(assetTransactionService.updateAssetTransaction(assetTransactionRequestModel, FOUND_ASSET_TRANSACTION_ID, FOUND_USER_ID))
                .thenReturn(assetTransactionResponseEntityModel);

        var assetTransactionResponseEntity = assetTransactionController.updateAssetTransaction(assetTransactionRequestModel, FOUND_ASSET_TRANSACTION_ID, FOUND_USER_ID);
        assertEquals(HttpStatus.OK, assetTransactionResponseEntity.getStatusCode());
        assertEquals(FOUND_ASSET_TRANSACTION_ID, assetTransactionResponseEntity.getBody().getAssetTransactionId());
        verify(assetTransactionService).updateAssetTransaction(assetTransactionRequestModel, FOUND_ASSET_TRANSACTION_ID, FOUND_USER_ID);
    }

    @Test
    void whenDeleteAssetTransaction_thenReturnNoContent() {
        doNothing().when(assetTransactionService)
                .removeAssetTransaction(FOUND_ASSET_TRANSACTION_ID, FOUND_USER_ID);

        var assetTransactionResponseEntity = assetTransactionController.removeAssetTransaction(FOUND_ASSET_TRANSACTION_ID, FOUND_USER_ID);
        assertEquals(HttpStatus.NO_CONTENT, assetTransactionResponseEntity.getStatusCode());
        verify(assetTransactionService).removeAssetTransaction(FOUND_ASSET_TRANSACTION_ID, FOUND_USER_ID);
    }
}