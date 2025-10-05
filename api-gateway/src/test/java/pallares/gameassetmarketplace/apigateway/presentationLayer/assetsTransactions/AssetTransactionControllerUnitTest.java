package pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pallares.gameassetmarketplace.apigateway.businessLayer.assetsTransactions.AssetTransactionService;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetTransactionControllerUnitTest {

    @Mock
    private AssetTransactionService service;

    @Mock
    private AssetTransactionModelAssembler assembler;

    @InjectMocks
    private AssetTransactionController controller;

    private String userId;
    private String txId;
    private AssetTransactionResponseModel sampleModel;
    private AssetTransactionRequestModel sampleRequest;

    @BeforeEach
    void setUp() {
        userId = "user-123";
        txId = "tx-456";
        sampleModel = AssetTransactionResponseModel.builder()
                .assetTransactionId(txId)
                .userId(userId)
                .build();
        sampleRequest = AssetTransactionRequestModel.builder()
                .userId(userId)
                .assetId("asset-789")
                .paymentId("pay-101")
                .build();
    }

    @Test
    void getAllReturnsListAndStatus200() {
        List<AssetTransactionResponseModel> list = List.of(sampleModel);
        when(service.getAllAssetTransactions(userId)).thenReturn(list);

        ResponseEntity<List<AssetTransactionResponseModel>> response = controller.getAll(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(list, response.getBody());
        verify(service).getAllAssetTransactions(userId);
    }

    @Test
    void getByIdReturnsModelAndStatus200() {
        when(service.getAssetTransactionByAssetTransactionId(txId, userId)).thenReturn(sampleModel);

        ResponseEntity<AssetTransactionResponseModel> response = controller.getById(userId, txId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(sampleModel, response.getBody());
        verify(service).getAssetTransactionByAssetTransactionId(txId, userId);
    }

    @Test
    void createReturnsModelAndStatus201() {
        when(service.addAssetTransaction(sampleRequest, userId)).thenReturn(sampleModel);

        ResponseEntity<AssetTransactionResponseModel> response = controller.create(userId, sampleRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(sampleModel, response.getBody());
        verify(service).addAssetTransaction(sampleRequest, userId);
    }

    @Test
    void updateReturnsModelAndStatus200() {
        when(service.updateAssetTransaction(sampleRequest, txId, userId)).thenReturn(sampleModel);

        ResponseEntity<AssetTransactionResponseModel> response = controller.update(userId, txId, sampleRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(sampleModel, response.getBody());
        verify(service).updateAssetTransaction(sampleRequest, txId, userId);
    }

    @Test
    void deleteReturnsNoContentAndInvokesService() {
        ResponseEntity<Void> response = controller.delete(userId, txId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).removeAssetTransaction(txId, userId);
    }
}
