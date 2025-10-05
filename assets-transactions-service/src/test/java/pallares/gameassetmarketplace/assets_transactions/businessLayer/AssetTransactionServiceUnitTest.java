package pallares.gameassetmarketplace.assets_transactions.businessLayer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.HttpClientErrorException;
import pallares.gameassetmarketplace.assets_transactions.dataAccessLayer.*;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.*;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments.*;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users.*;
import pallares.gameassetmarketplace.assets_transactions.mappingLayer.AssetTransactionRequestMapper;
import pallares.gameassetmarketplace.assets_transactions.mappingLayer.AssetTransactionResponseMapper;
import pallares.gameassetmarketplace.assets_transactions.presentationLayer.AssetTransactionRequestModel;
import pallares.gameassetmarketplace.assets_transactions.presentationLayer.AssetTransactionResponseModel;
import pallares.gameassetmarketplace.assets_transactions.utils.exceptions.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration")
@ActiveProfiles("test")
class AssetTransactionServiceUnitTest {

    @Autowired
    AssetTransactionService assetTransactionService;

    @MockitoBean
    AssetTransactionRepository assetTransactionRepository;

    @MockitoBean
    AssetTransactionResponseMapper assetTransactionResponseMapper;

    @MockitoBean
    AssetTransactionRequestMapper assetTransactionRequestMapper;

    @MockitoBean
    UsersServiceClient usersServiceClient;

    @MockitoBean
    PaymentsServiceClient paymentsServiceClient;

    @MockitoBean
    AssetsServiceClient assetsServiceClient;

    private final String USER_ID = "user-123";
    private final String TX_ID = "tx-789";
    private final String ASSET_ID = "asset-123";
    private final String PAYMENT_ID = "payment-123";

    // --- ADD AssetTransaction tests ---
    @Test
    public void whenValidAddAssetTransaction_thenReturnResponseModel() {
        // existing test unchanged...
        when(assetsServiceClient.getAssetByAssetId(ASSET_ID)).thenReturn(AssetModel.builder().assetId(ASSET_ID).name("Name").assetType(AssetType.MODEL).licenseType(LicenseType.ROYALTY_FREE).build());
        when(paymentsServiceClient.getPaymentByPaymentId(PAYMENT_ID)).thenReturn(PaymentModel.builder().paymentId(PAYMENT_ID).price(new BigDecimal("20.00")).currency(Currency.US).build());
        when(usersServiceClient.getUserByUserId(USER_ID)).thenReturn(UserModel.builder().userId(USER_ID).username("u").build());
        when(assetTransactionRepository.existsByUserModel_UserIdAndAssetModel_AssetId(USER_ID, ASSET_ID)).thenReturn(false);
        AssetTransaction entity = AssetTransaction.builder().assetTransactionIdentifier(new AssetTransactionIdentifier()).userModel(UserModel.builder().userId(USER_ID).build()).paymentModel(PaymentModel.builder().paymentId(PAYMENT_ID).price(new BigDecimal("20.00")).build()).assetModel(AssetModel.builder().assetId(ASSET_ID).build()).status(AssetTransactionStatus.PAID).type(AssetTransactionType.SINGLE_ASSET).discount(new Discount(new BigDecimal("0"),BigDecimal.ZERO,"")).build();
        AssetTransactionRequestModel req = AssetTransactionRequestModel.builder().assetId(ASSET_ID).paymentId(PAYMENT_ID).status(AssetTransactionStatus.PAID).type(AssetTransactionType.SINGLE_ASSET).discount(new Discount(BigDecimal.ZERO,BigDecimal.ZERO,"")).build();
        when(assetTransactionRequestMapper.requestModelToEntity(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(entity);
        when(assetTransactionRepository.save(entity)).thenReturn(entity);
        AssetTransactionResponseModel resp = AssetTransactionResponseModel.builder().assetId(ASSET_ID).userId(USER_ID).paymentId(PAYMENT_ID).status(AssetTransactionStatus.PAID).type(AssetTransactionType.SINGLE_ASSET).discount(new Discount(BigDecimal.ZERO,BigDecimal.ZERO,"")).build();
        when(assetTransactionResponseMapper.entityToResponseModel(entity)).thenReturn(resp);

        AssetTransactionResponseModel result = assetTransactionService.addAssetTransaction(req, USER_ID);
        assertEquals(ASSET_ID, result.getAssetId());
        assertEquals(USER_ID, result.getUserId());
    }

    // --- GET ALL ---
    @Test
    public void whenGetAllAssetTransactionsAndUserExists_thenReturnList() {
        UserModel user = UserModel.builder().userId(USER_ID).username("u").build();
        when(usersServiceClient.getUserByUserId(USER_ID)).thenReturn(user);
        AssetTransaction tx1 = mock(AssetTransaction.class);
        AssetTransaction tx2 = mock(AssetTransaction.class);
        when(assetTransactionRepository.findAllByUserModel_UserId(USER_ID)).thenReturn(List.of(tx1, tx2));
        AssetTransactionResponseModel m1 = new AssetTransactionResponseModel();
        AssetTransactionResponseModel m2 = new AssetTransactionResponseModel();
        when(assetTransactionResponseMapper.entityListToResponseModelList(List.of(tx1, tx2))).thenReturn(List.of(m1, m2));

        List<AssetTransactionResponseModel> result = assetTransactionService.getAllAssetTransactions(USER_ID);
        assertEquals(2, result.size());
    }

    @Test
    public void whenGetAllAssetTransactionsAndUserNotFound_thenThrowNotFound() {
        when(usersServiceClient.getUserByUserId(USER_ID)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> assetTransactionService.getAllAssetTransactions(USER_ID));
    }

    // --- GET BY ID ---
    @Test
    public void whenGetByIdAndFound_thenReturnResponseModel() {
        AssetTransaction tx = mock(AssetTransaction.class);
        when(assetTransactionRepository.findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(TX_ID, USER_ID)).thenReturn(tx);
        AssetTransactionResponseModel resp = new AssetTransactionResponseModel();
        when(assetTransactionResponseMapper.entityToResponseModel(tx)).thenReturn(resp);
        AssetTransactionResponseModel result = assetTransactionService.getAssetTransactionByAssetTransactionId(TX_ID, USER_ID);
        assertNotNull(result);
    }

    @Test
    public void whenGetByIdNotFound_thenThrowNotFoundException() {
        when(assetTransactionRepository.findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(TX_ID, USER_ID)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> assetTransactionService.getAssetTransactionByAssetTransactionId(TX_ID, USER_ID));
    }

    // --- UPDATE ---
    @Test
    public void whenValidUpdateAssetTransaction_thenReturnUpdatedModel() {
        AssetTransaction existing = mock(AssetTransaction.class);
        when(assetTransactionRepository.findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(TX_ID, USER_ID)).thenReturn(existing);
        AssetTransactionRequestModel req = AssetTransactionRequestModel.builder().assetId(ASSET_ID).paymentId(PAYMENT_ID).status(AssetTransactionStatus.PAID).type(AssetTransactionType.SINGLE_ASSET).discount(new Discount(BigDecimal.ZERO,BigDecimal.ZERO,"")).build();
        when(assetsServiceClient.getAssetByAssetId(ASSET_ID)).thenReturn(AssetModel.builder().assetId(ASSET_ID).build());
        when(paymentsServiceClient.getPaymentByPaymentId(PAYMENT_ID)).thenReturn(PaymentModel.builder().paymentId(PAYMENT_ID).price(new BigDecimal("5")).price(new BigDecimal("5")).build());
        when(usersServiceClient.getUserByUserId(USER_ID)).thenReturn(UserModel.builder().userId(USER_ID).build());
        when(assetTransactionRepository.save(existing)).thenReturn(existing);
        AssetTransactionResponseModel out = new AssetTransactionResponseModel();
        when(assetTransactionResponseMapper.entityToResponseModel(existing)).thenReturn(out);

        AssetTransactionResponseModel result = assetTransactionService.updateAssetTransaction(req, TX_ID, USER_ID);
        assertSame(out, result);
    }

    @Test
    public void whenUpdateNotFound_thenThrowNotFound() {
        when(assetTransactionRepository.findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(TX_ID, USER_ID)).thenReturn(null);
        AssetTransactionRequestModel req = AssetTransactionRequestModel.builder().assetId(ASSET_ID).paymentId(PAYMENT_ID).status(AssetTransactionStatus.PAID).type(AssetTransactionType.SINGLE_ASSET).discount(new Discount(BigDecimal.ZERO,BigDecimal.ZERO,"")).build();
        assertThrows(NotFoundException.class, () -> assetTransactionService.updateAssetTransaction(req, TX_ID, USER_ID));
    }

    @Test
    public void whenUpdateChangePaidToOther_thenThrowInvalidTransactionStateException() {
        AssetTransaction existing = AssetTransaction.builder().assetTransactionIdentifier(new AssetTransactionIdentifier()).status(AssetTransactionStatus.PAID).userModel(UserModel.builder().userId(USER_ID).build()).paymentModel(PaymentModel.builder().paymentId(PAYMENT_ID).price(new BigDecimal("1")).price(new BigDecimal("1")).build()).assetModel(AssetModel.builder().assetId(ASSET_ID).build()).discount(new Discount(BigDecimal.ZERO,BigDecimal.ZERO,"")).type(AssetTransactionType.SINGLE_ASSET).build();
        when(assetTransactionRepository.findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(TX_ID, USER_ID)).thenReturn(existing);
        AssetTransactionRequestModel req = AssetTransactionRequestModel.builder().assetId(ASSET_ID).paymentId(PAYMENT_ID).status(AssetTransactionStatus.PENDING).type(AssetTransactionType.SINGLE_ASSET).discount(new Discount(BigDecimal.ZERO,BigDecimal.ZERO,"")).build();
        when(assetsServiceClient.getAssetByAssetId(ASSET_ID)).thenReturn(AssetModel.builder().assetId(ASSET_ID).build());
        when(paymentsServiceClient.getPaymentByPaymentId(PAYMENT_ID)).thenReturn(PaymentModel.builder().paymentId(PAYMENT_ID).price(new BigDecimal("1")).price(new BigDecimal("1")).build());
        when(usersServiceClient.getUserByUserId(USER_ID)).thenReturn(UserModel.builder().userId(USER_ID).build());
        assertThrows(InvalidTransactionStateException.class, () -> assetTransactionService.updateAssetTransaction(req, TX_ID, USER_ID));
    }

    @Test
    public void whenRemoveSuccessful_thenNoException() {
        AssetTransaction existing = mock(AssetTransaction.class);
        when(assetTransactionRepository.findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(TX_ID, USER_ID)).thenReturn(existing);
        assertDoesNotThrow(() -> assetTransactionService.removeAssetTransaction(TX_ID, USER_ID));
        verify(assetTransactionRepository).delete(existing);
    }

    @Test
    public void whenRemoveNotFound_thenThrowNotFound() {
        when(assetTransactionRepository.findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(TX_ID, USER_ID)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> assetTransactionService.removeAssetTransaction(TX_ID, USER_ID));
    }

    @Test
    public void whenAddAssetTransactionAndAssetNotFound_thenThrowNotFoundException() {
        var req = AssetTransactionRequestModel.builder()
                .assetId(ASSET_ID)
                .paymentId(PAYMENT_ID)
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(BigDecimal.ZERO, BigDecimal.ZERO, ""))
                .build();

        when(assetsServiceClient.getAssetByAssetId(ASSET_ID)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> assetTransactionService.addAssetTransaction(req, USER_ID));
    }

    @Test
    public void whenAddAssetTransactionAndPaymentNotFound_thenThrowNotFoundException() {
        when(assetsServiceClient.getAssetByAssetId(ASSET_ID))
                .thenReturn(AssetModel.builder().assetId(ASSET_ID).build());

        var req = AssetTransactionRequestModel.builder()
                .assetId(ASSET_ID)
                .paymentId(PAYMENT_ID)
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(BigDecimal.ZERO, BigDecimal.ZERO, ""))
                .build();

        when(paymentsServiceClient.getPaymentByPaymentId(PAYMENT_ID)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> assetTransactionService.addAssetTransaction(req, USER_ID));
    }

    @Test
    public void whenAddAssetTransactionAndPaymentPriceNull_thenThrowIllegalStateException() {
        when(assetsServiceClient.getAssetByAssetId(ASSET_ID))
                .thenReturn(AssetModel.builder().assetId(ASSET_ID).build());
        when(paymentsServiceClient.getPaymentByPaymentId(PAYMENT_ID))
                .thenReturn(PaymentModel.builder().paymentId(PAYMENT_ID).price(null).build());

        var req = AssetTransactionRequestModel.builder()
                .assetId(ASSET_ID)
                .paymentId(PAYMENT_ID)
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(BigDecimal.ZERO, BigDecimal.ZERO, ""))
                .build();

        assertThrows(IllegalStateException.class,
                () -> assetTransactionService.addAssetTransaction(req, USER_ID));
    }

    @Test
    public void whenUpdateAssetTransactionAndAssetNotFound_thenThrowNotFoundException() {
        when(assetTransactionRepository
                .findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(TX_ID, USER_ID))
                .thenReturn(mock(AssetTransaction.class));

        var req = AssetTransactionRequestModel.builder()
                .assetId(ASSET_ID)
                .paymentId(PAYMENT_ID)
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(BigDecimal.ZERO, BigDecimal.ZERO, ""))
                .build();

        when(assetsServiceClient.getAssetByAssetId(ASSET_ID)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> assetTransactionService.updateAssetTransaction(req, TX_ID, USER_ID));
    }

    @Test
    public void whenUpdateAssetTransactionAndPaymentNotFound_thenThrowNotFoundException() {
        when(assetTransactionRepository
                .findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(TX_ID, USER_ID))
                .thenReturn(mock(AssetTransaction.class));

        when(assetsServiceClient.getAssetByAssetId(ASSET_ID))
                .thenReturn(AssetModel.builder().assetId(ASSET_ID).build());

        var req = AssetTransactionRequestModel.builder()
                .assetId(ASSET_ID)
                .paymentId(PAYMENT_ID)
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(BigDecimal.ZERO, BigDecimal.ZERO, ""))
                .build();

        when(paymentsServiceClient.getPaymentByPaymentId(PAYMENT_ID)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> assetTransactionService.updateAssetTransaction(req, TX_ID, USER_ID));
    }

    @Test
    public void whenUpdateAssetTransactionAndPaymentPriceNull_thenThrowIllegalStateException() {
        when(assetTransactionRepository
                .findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(TX_ID, USER_ID))
                .thenReturn(mock(AssetTransaction.class));

        when(assetsServiceClient.getAssetByAssetId(ASSET_ID))
                .thenReturn(AssetModel.builder().assetId(ASSET_ID).build());
        when(paymentsServiceClient.getPaymentByPaymentId(PAYMENT_ID))
                .thenReturn(PaymentModel.builder().paymentId(PAYMENT_ID).price(null).build());

        var req = AssetTransactionRequestModel.builder()
                .assetId(ASSET_ID)
                .paymentId(PAYMENT_ID)
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(BigDecimal.ZERO, BigDecimal.ZERO, ""))
                .build();

        assertThrows(IllegalStateException.class,
                () -> assetTransactionService.updateAssetTransaction(req, TX_ID, USER_ID));
    }

    @Test
    public void whenUpdateAssetTransactionAndUserNotFound_thenThrowNotFoundException() {
        var existing = mock(AssetTransaction.class);
        when(assetTransactionRepository
                .findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(TX_ID, USER_ID))
                .thenReturn(existing);

        when(assetsServiceClient.getAssetByAssetId(ASSET_ID))
                .thenReturn(AssetModel.builder().assetId(ASSET_ID).build());
        when(paymentsServiceClient.getPaymentByPaymentId(PAYMENT_ID))
                .thenReturn(PaymentModel.builder().paymentId(PAYMENT_ID).price(BigDecimal.ONE).build());

        // simulate no user
        when(usersServiceClient.getUserByUserId(USER_ID)).thenReturn(null);

        var req = AssetTransactionRequestModel.builder()
                .assetId(ASSET_ID)
                .paymentId(PAYMENT_ID)
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(BigDecimal.ZERO, BigDecimal.ZERO, ""))
                .build();

        assertThrows(NotFoundException.class,
                () -> assetTransactionService.updateAssetTransaction(req, TX_ID, USER_ID));
    }

    @Test
    public void whenUpdateAssetTransactionAndInvalidDiscount_thenThrowInvalidDiscountException() {
        var existing = mock(AssetTransaction.class);
        when(assetTransactionRepository
                .findByAssetTransactionIdentifier_AssetTransactionIdAndUserModel_UserId(TX_ID, USER_ID))
                .thenReturn(existing);

        when(assetsServiceClient.getAssetByAssetId(ASSET_ID))
                .thenReturn(AssetModel.builder().assetId(ASSET_ID).build());
        when(paymentsServiceClient.getPaymentByPaymentId(PAYMENT_ID))
                .thenReturn(PaymentModel.builder().paymentId(PAYMENT_ID).price(BigDecimal.ONE).build());
        when(usersServiceClient.getUserByUserId(USER_ID))
                .thenReturn(UserModel.builder().userId(USER_ID).build());

        // percentage > 100%
        var req = AssetTransactionRequestModel.builder()
                .assetId(ASSET_ID)
                .paymentId(PAYMENT_ID)
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(new BigDecimal("150"), BigDecimal.ZERO, "too big"))
                .build();

        assertThrows(InvalidDiscountException.class,
                () -> assetTransactionService.updateAssetTransaction(req, TX_ID, USER_ID));
    }

    @Test
    public void whenUsersClientThrowsHttpClientError_thenHandleHttpClientException() {
        HttpClientErrorException exception = createHttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        when(usersServiceClient.getUserByUserId(USER_ID)).thenThrow(exception);

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> assetTransactionService.getAllAssetTransactions(USER_ID));

        assertTrue(thrown.getMessage().contains("User not found"));
    }

    @Test
    public void whenPaymentsClientThrowsHttpClientError_thenHandleHttpClientException() {
        AssetTransactionRequestModel req = AssetTransactionRequestModel.builder()
                .assetId(ASSET_ID)
                .paymentId(PAYMENT_ID)
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(BigDecimal.ZERO, BigDecimal.ZERO, ""))
                .build();

        when(assetsServiceClient.getAssetByAssetId(ASSET_ID)).thenReturn(
                AssetModel.builder().assetId(ASSET_ID).build());

        HttpClientErrorException exception = createHttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid payment");
        when(paymentsServiceClient.getPaymentByPaymentId(PAYMENT_ID)).thenThrow(exception);

        assertThrows(InvalidInputException.class,
                () -> assetTransactionService.addAssetTransaction(req, USER_ID));
    }

    @Test
    public void whenAssetsClientThrowsHttpClientError_thenHandleHttpClientException() {
        AssetTransactionRequestModel req = AssetTransactionRequestModel.builder()
                .assetId(ASSET_ID)
                .paymentId(PAYMENT_ID)
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(BigDecimal.ZERO, BigDecimal.ZERO, ""))
                .build();

        HttpClientErrorException exception = createHttpClientErrorException(HttpStatus.NOT_FOUND, "Asset not found");
        when(assetsServiceClient.getAssetByAssetId(ASSET_ID)).thenThrow(exception);

        assertThrows(NotFoundException.class,
                () -> assetTransactionService.addAssetTransaction(req, USER_ID));
    }

    private HttpClientErrorException createHttpClientErrorException(HttpStatus status, String message) {
        return HttpClientErrorException.create(
                status,
                status.getReasonPhrase(),
                HttpHeaders.readOnlyHttpHeaders(new HttpHeaders()),
                message.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8);
    }

}
