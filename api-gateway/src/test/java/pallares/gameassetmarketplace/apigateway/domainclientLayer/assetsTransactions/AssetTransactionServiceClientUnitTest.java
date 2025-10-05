package pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions.AssetTransactionRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions.AssetTransactionResponseModel;
import pallares.gameassetmarketplace.apigateway.utils.exceptions.InvalidInputException;
import pallares.gameassetmarketplace.apigateway.utils.exceptions.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetTransactionServiceClientUnitTest {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private AssetTransactionServiceClient client;

    private final String host = "localhost";
    private final String port = "8083";
    private final String userId = "user123";
    private final String txId = "tx456";

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        objectMapper = new ObjectMapper();
        client = new AssetTransactionServiceClient(restTemplate, objectMapper, host, port);
    }

    @Test
    void getAllAssetTransactions_returnsList() {
        // build a single mock response
        AssetTransactionResponseModel[] mockResponse = new AssetTransactionResponseModel[] {
                AssetTransactionResponseModel.builder()
                        .assetTransactionId("tx1")
                        .assetId("asset1")
                        .price(new BigDecimal("100"))
                        .build()
        };
        ResponseEntity<AssetTransactionResponseModel[]> entity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(),
                ArgumentMatchers.<Class<AssetTransactionResponseModel[]>>any()))
                .thenReturn(entity);

        List<AssetTransactionResponseModel> result = client.getAllAssetTransactions(userId);

        assertEquals(1, result.size());
        assertEquals("tx1", result.get(0).getAssetTransactionId());
    }

    @Test
    void getAssetTransactionById_returnsOne() {
        AssetTransactionResponseModel mockResponse = AssetTransactionResponseModel.builder()
                .assetTransactionId("tx1")
                .assetId("asset1")
                .price(new BigDecimal("50"))
                .build();

        when(restTemplate.getForObject(anyString(), eq(AssetTransactionResponseModel.class)))
                .thenReturn(mockResponse);

        AssetTransactionResponseModel result = client.getAssetTransactionById("tx1", userId);
        assertNotNull(result);
        assertEquals("tx1", result.getAssetTransactionId());
    }

    @Test
    void addAssetTransaction_returnsCreatedTransaction() {
        AssetTransactionRequestModel request = AssetTransactionRequestModel.builder()
                .userId(userId)
                .assetId("asset1")
                .paymentId("pay1")
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(new BigDecimal("0.1"), new BigDecimal("5.0"), "test"))
                .build();

        AssetTransactionResponseModel mockResponse = AssetTransactionResponseModel.builder()
                .assetTransactionId("tx1")
                .assetId("asset1")
                .price(new BigDecimal("75"))
                .build();

        when(restTemplate.postForObject(anyString(), eq(request), eq(AssetTransactionResponseModel.class)))
                .thenReturn(mockResponse);

        AssetTransactionResponseModel result = client.addAssetTransaction(request, userId);
        assertEquals("tx1", result.getAssetTransactionId());
    }

    @Test
    void updateAssetTransaction_returnsUpdatedTransaction() {
        AssetTransactionRequestModel request = AssetTransactionRequestModel.builder()
                .userId(userId)
                .assetId("asset1")
                .paymentId("pay1")
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SUBSCRIPTION)
                .discount(new Discount(new BigDecimal("0.2"), new BigDecimal("10.0"), "promo"))
                .build();

        AssetTransactionResponseModel updated = AssetTransactionResponseModel.builder()
                .assetTransactionId(txId)
                .assetId("asset1")
                .price(new BigDecimal("200"))
                .build();

        doNothing().when(restTemplate).put(anyString(), eq(request));
        when(restTemplate.getForObject(anyString(), eq(AssetTransactionResponseModel.class)))
                .thenReturn(updated);

        AssetTransactionResponseModel result = client.updateAssetTransaction(request, txId, userId);
        assertEquals(new BigDecimal("200"), result.getPrice());
    }

    @Test
    void removeAssetTransaction_executesWithoutError() {
        doNothing().when(restTemplate).delete(anyString());
        assertDoesNotThrow(() -> client.removeAssetTransaction(txId, userId));
    }

    @Test
    void getAssetTransactionById_throwsNotFoundException() {
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.NOT_FOUND,
                "Not Found",
                HttpHeaders.EMPTY,
                "{\"message\":\"Not found\"}".getBytes(),
                null
        );
        when(restTemplate.getForObject(anyString(), eq(AssetTransactionResponseModel.class)))
                .thenThrow(ex);

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> client.getAssetTransactionById("badId", userId));
        assertEquals("Not found", thrown.getMessage());
    }

    @Test
    void addAssetTransaction_throwsInvalidInputException() {
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Unprocessable Entity",
                HttpHeaders.EMPTY,
                "{\"message\":\"Invalid input\"}".getBytes(),
                null
        );
        AssetTransactionRequestModel request = AssetTransactionRequestModel.builder()
                .userId(userId)
                .assetId("bad")
                .paymentId("payX")
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(new BigDecimal("1.5"), new BigDecimal("-5.0"), "invalid"))
                .build();

        when(restTemplate.postForObject(anyString(), eq(request), eq(AssetTransactionResponseModel.class)))
                .thenThrow(ex);

        InvalidInputException thrown = assertThrows(InvalidInputException.class,
                () -> client.addAssetTransaction(request, userId));
        assertEquals("Invalid input", thrown.getMessage());
    }

    @Test
    void removeAssetTransaction_unexpectedError() {
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error",
                HttpHeaders.EMPTY,
                "Server error".getBytes(),
                null
        );
        doThrow(ex).when(restTemplate).delete(anyString());

        HttpClientErrorException thrown = assertThrows(HttpClientErrorException.class,
                () -> client.removeAssetTransaction(txId, userId));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatusCode());
    }
}
