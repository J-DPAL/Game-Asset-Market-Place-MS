package pallares.gameassetmarketplace.apigateway.businessLayer.assetsTransactions;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.InOrder;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions.AssetTransactionServiceClient;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions.AssetTransactionStatus;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions.AssetTransactionType;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions.AssetTransactionRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions.AssetTransactionResponseModel;

class AssetTransactionServiceUnitTest {

    @Mock
    private AssetTransactionServiceClient transactionClient;

    @InjectMocks
    private AssetTransactionServiceImpl service;

    private AssetTransactionResponseModel sampleResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleResponse = AssetTransactionResponseModel.builder()
                .assetTransactionId("tx-456")
                .userId("user-123")
                .assetId("asset-001")
                .name("Sample Asset")
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .build();
    }

    @Test
    void getAllAssetTransactions_delegatesToClient() {
        when(transactionClient.getAllAssetTransactions("user-123"))
                .thenReturn(List.of(sampleResponse));

        var result = service.getAllAssetTransactions("user-123");

        assertEquals(1, result.size());
        assertSame(sampleResponse, result.get(0));
        verify(transactionClient).getAllAssetTransactions("user-123");
    }

    @Test
    void getAssetTransactionById_delegatesToClient() {
        when(transactionClient.getAssetTransactionById("tx-456", "user-123"))
                .thenReturn(sampleResponse);

        var result = service.getAssetTransactionByAssetTransactionId("tx-456", "user-123");

        assertEquals("tx-456", result.getAssetTransactionId());
        verify(transactionClient).getAssetTransactionById("tx-456", "user-123");
    }

    @Test
    void addAssetTransaction_delegatesToClient() {
        var req = new AssetTransactionRequestModel();
        when(transactionClient.addAssetTransaction(req, "user-123"))
                .thenReturn(sampleResponse);

        var result = service.addAssetTransaction(req, "user-123");

        assertSame(sampleResponse, result);
        verify(transactionClient).addAssetTransaction(req, "user-123");
    }

    @Test
    void updateAssetTransaction_callsUpdateThenFetch_withCorrectParameterOrder() {
        var req = new AssetTransactionRequestModel();
        // stub update (returns void or null)
        when(transactionClient.updateAssetTransaction(req, "tx-456", "user-123")).thenReturn(null);
        // note: code erroneously calls getAssetTransactionById(userId, txId)
        when(transactionClient.getAssetTransactionById("tx-456", "user-123"))
                .thenReturn(sampleResponse);

        var result = service.updateAssetTransaction(req, "tx-456", "user-123");

        assertSame(sampleResponse, result);
        InOrder inOrder = inOrder(transactionClient);
        inOrder.verify(transactionClient).updateAssetTransaction(req, "tx-456", "user-123");
        inOrder.verify(transactionClient).getAssetTransactionById("tx-456", "user-123");
    }

    @Test
    void removeAssetTransaction_delegatesToClient() {
        doNothing().when(transactionClient).removeAssetTransaction("tx-456", "user-123");

        service.removeAssetTransaction("tx-456", "user-123");

        verify(transactionClient).removeAssetTransaction("tx-456", "user-123");
    }
}
