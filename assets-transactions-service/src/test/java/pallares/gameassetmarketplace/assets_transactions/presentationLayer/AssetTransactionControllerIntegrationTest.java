package pallares.gameassetmarketplace.assets_transactions.presentationLayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;
import pallares.gameassetmarketplace.assets_transactions.dataAccessLayer.AssetTransactionRepository;
import pallares.gameassetmarketplace.assets_transactions.dataAccessLayer.AssetTransactionStatus;
import pallares.gameassetmarketplace.assets_transactions.dataAccessLayer.AssetTransactionType;
import pallares.gameassetmarketplace.assets_transactions.dataAccessLayer.Discount;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.AssetModel;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.AssetType;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.LicenseType;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AssetTransactionControllerIntegrationTest {
    @Autowired
    WebTestClient webClient;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockRestServiceServer;

    private ObjectMapper mapper= new ObjectMapper();

    private final String BASE_URI = "/api/v1/users";
    private final String FOUND_ASSET_TRANSACTION_ID = "a1b2c3d4-e5f6-7890-1234-56789abcdef0";
    private final String NOT_FOUND_ASSET_TRANSACTION_ID = "p1b7r3d4-e5f6-1234-0946-56789abcdef0";
    private final String INVALID_ASSET_TRANSACTION_ID = "c9040au79-cb47-6d96-888e-ff96908";
    private final String FOUND_USER_ID = "c5440a89-cb47-4d96-888e-yy96708db4d9";
    private final String BASE_URI_ASSETS = "http://localhost:7001/api/v1/assets";
    private final String BASE_URI_PAYMENTS = "http://localhost:7002/api/v1/payments";
    private final String BASE_URI_USERS = "http://localhost:7003/api/v1/users";

    private AssetTransactionRequestModel assetTransactionRequestModel = createAssetTransactionRequestModel();

    @Autowired
    private AssetTransactionRepository assetTransactionRepository;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void init() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        assertTrue(assetTransactionRepository.count() > 0);
    }

    @Test
    public void whenAssetTransactionIdIsInvalid_thenReturnUnprocessableEntity() {
        webTestClient.get()
                .uri(BASE_URI + "/" + FOUND_USER_ID + "/asset_transactions/" + INVALID_ASSET_TRANSACTION_ID)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void whenAssetTransactionDoesNotExist_thenReturnNotFound() {
        webTestClient.get()
                .uri(BASE_URI + "/" + FOUND_USER_ID + "/asset_transactions/" + NOT_FOUND_ASSET_TRANSACTION_ID)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenDeletingInvalidAssetTransactionId_thenReturnUnprocessableEntity() {
        webTestClient.delete()
                .uri(BASE_URI + "/" + FOUND_USER_ID + "/asset_transactions/" + INVALID_ASSET_TRANSACTION_ID)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void whenDeletingNonExistingAssetTransactionId_thenReturnNotFound() {
        webTestClient.delete()
                .uri(BASE_URI + "/" + FOUND_USER_ID + "/asset_transactions/" + NOT_FOUND_ASSET_TRANSACTION_ID)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenGetAllAssetTransactions_thenReturnCorrectCount() throws URISyntaxException {
        // 1. Expect user service call
        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI(BASE_URI_USERS + "/" + FOUND_USER_ID)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"userId\":\"" + FOUND_USER_ID + "\"}"));

        // 2. Make the call to your API
        webTestClient.get()
                .uri(BASE_URI + "/" + FOUND_USER_ID + "/asset_transactions")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AssetTransactionResponseModel.class)
                .value(assetTransactions -> {
                    assertNotNull(assetTransactions);
                    assertEquals(2, assetTransactions.size());
                });
    }


    @Test
    public void whenValidPostRequest_thenReturnCreatedTransaction() throws JsonProcessingException, URISyntaxException {
        // 1. Expect asset service call
        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI(BASE_URI_ASSETS + "/" + assetTransactionRequestModel.getAssetId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(AssetModel.builder()
                                .assetId(assetTransactionRequestModel.getAssetId())
                                .name("Sample Asset")
                                .description("Description")
                                .assetType(AssetType.MODEL)
                                .fileUrl("https://example.com")
                                .thumbnailUrl("https://example.com/thumb")
                                .licenseType(LicenseType.ROYALTY_FREE)
                                .build())));

        // 2. Expect payment service call
        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI(BASE_URI_PAYMENTS + "/" + assetTransactionRequestModel.getPaymentId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"paymentId\":\"" + assetTransactionRequestModel.getPaymentId() + "\", \"price\":49.99}")); // <-- FIXED

        // 3. Expect user service call
        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI(BASE_URI_USERS + "/" + assetTransactionRequestModel.getUserId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"userId\":\"" + assetTransactionRequestModel.getUserId() + "\"}"));

        // Post the request and validate
        webTestClient.post()
                .uri(BASE_URI + "/" + FOUND_USER_ID + "/asset_transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(assetTransactionRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AssetTransactionResponseModel.class)
                .value(response -> {
                    assertNotNull(response);
                    assertEquals(assetTransactionRequestModel.getAssetId(), response.getAssetId());
                    assertEquals(assetTransactionRequestModel.getUserId(), response.getUserId());
                    assertEquals(assetTransactionRequestModel.getPaymentId(), response.getPaymentId());
                });
    }

    @Test
    public void whenPostingDuplicateTransaction_thenReturnConflict() throws JsonProcessingException, URISyntaxException {
        // These values match what is loaded by DatabaseLoaderService
        String existingUserId = "c5440a89-cb47-4d96-888e-yy96708db4d9";
        String existingAssetId = "c6651849-c406-4563-a937-a441d380ff25";
        String existingPaymentId = "550e8400-e29b-41d4-a716-446655440000";

        AssetTransactionRequestModel duplicateRequest = AssetTransactionRequestModel.builder()
                .assetId(existingAssetId)
                .userId(existingUserId)
                .paymentId(existingPaymentId)
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(new BigDecimal("0.10"), new BigDecimal("4.99"), "Boxing Day Discount"))
                .build();

        // 1. Expect asset service call
        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI(BASE_URI_ASSETS + "/" + existingAssetId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(AssetModel.builder()
                                .assetId(existingAssetId)
                                .name("Existing Asset")
                                .description("Already exists")
                                .assetType(AssetType.MODEL)
                                .fileUrl("https://example.com/exists")
                                .thumbnailUrl("https://example.com/exists/thumb")
                                .licenseType(LicenseType.ROYALTY_FREE)
                                .build())));

        // 2. Expect payment service call
        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI(BASE_URI_PAYMENTS + "/" + existingPaymentId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                                .body("{\"paymentId\":\"" + existingPaymentId + "\", \"price\":49.99}"));

        // 3. Expect user service call
        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo(new URI(BASE_URI_USERS + "/" + existingUserId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"userId\":\"" + existingUserId + "\"}"));

        // Act
        webTestClient.post()
                .uri(BASE_URI + "/" + FOUND_USER_ID + "/asset_transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(duplicateRequest)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .jsonPath("$.message").value(msg -> assertTrue(((String) msg).contains("already purchased this asset")));
    }


    private AssetTransactionRequestModel createAssetTransactionRequestModel() {
        var assetTransactionRequestModel = AssetTransactionRequestModel.builder()
                .assetId("c6651849-c406-4563-a937-a441d380ff24")
                .userId("c5440a89-cb47-4d96-888e-yy96708db4d9")
                .paymentId("550e8400-e29b-41d4-a716-446655440000")
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(new BigDecimal("0.10"), new BigDecimal("4.99"), "Boxing Day Discount"))
                .build();

        return assetTransactionRequestModel;
    }
}
