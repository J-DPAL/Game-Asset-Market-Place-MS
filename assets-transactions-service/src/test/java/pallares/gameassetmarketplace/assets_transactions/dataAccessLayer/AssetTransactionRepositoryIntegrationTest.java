package pallares.gameassetmarketplace.assets_transactions.dataAccessLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.AssetModel;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.AssetType;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.LicenseType;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments.Currency;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments.PaymentModel;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments.PaymentType;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments.TransactionStatus;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users.EmailAddress;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users.PhoneNumber;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users.UserModel;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class AssetTransactionRepositoryIntegrationTest {

    @Autowired
    AssetTransactionRepository assetTransactionRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private AssetTransaction assetTransaction1;
    private AssetTransaction assetTransaction2;

    @BeforeEach
    public void setUp() {
        mongoTemplate.dropCollection(AssetTransaction.class);

        // First transaction
        AssetModel assetModel1 = AssetModel.builder()
                .assetId("c6651849-c406-4563-a937-a441d380ff25")
                .name("Sci-Fi Spaceship")
                .description("A high-quality 3D spaceship model.")
                .assetType(AssetType.MODEL)
                .fileUrl("https://example.com/files/spaceship.obj")
                .thumbnailUrl("https://example.com/thumbnails/spaceship.jpg")
                .licenseType(LicenseType.ROYALTY_FREE)
                .build();

        PaymentModel paymentModel1 = PaymentModel.builder()
                .paymentId("550e8400-e29b-41d4-a716-446655440000")
                .price(new BigDecimal("49.99"))
                .currency(Currency.US)
                .paymentType(PaymentType.CREDIT_CARD)
                .transactionStatus(TransactionStatus.COMPLETED)
                .build();

        UserModel userModel1 = UserModel.builder()
                .userId("c5440a89-cb47-4d96-888e-yy96708db4d9")
                .username("AU")
                .emailAddress(new EmailAddress("aucceli0@dot.gov"))
                .phoneNumber(new PhoneNumber("526-461-4430"))
                .build();

        assetTransaction1 = AssetTransaction.builder()
                .assetTransactionIdentifier(new AssetTransactionIdentifier())
                .assetModel(assetModel1)
                .paymentModel(paymentModel1)
                .userModel(userModel1)
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(new BigDecimal("0.10"), new BigDecimal("4.99"), "Boxing Day Discount"))
                .build();

        // Second transaction (different user & asset)
        AssetModel assetModel2 = AssetModel.builder()
                .assetId("11111111-1111-1111-1111-111111111111")
                .name("Fantasy Castle")
                .description("A medieval castle model.")
                .assetType(AssetType.MODEL)
                .fileUrl("https://example.com/files/castle.obj")
                .thumbnailUrl("https://example.com/thumbnails/castle.jpg")
                .licenseType(LicenseType.ROYALTY_FREE)
                .build();

        PaymentModel paymentModel2 = PaymentModel.builder()
                .paymentId("22222222-2222-2222-2222-222222222222")
                .price(new BigDecimal("79.99"))
                .currency(Currency.EUR)
                .paymentType(PaymentType.PAYPAL)
                .transactionStatus(TransactionStatus.PENDING)
                .build();

        UserModel userModel2 = UserModel.builder()
                .userId("d1234567-89ab-cdef-0123-456789abcdef")
                .username("BB")
                .emailAddress(new EmailAddress("bbuser@example.com"))
                .phoneNumber(new PhoneNumber("123-456-7890"))
                .build();

        assetTransaction2 = AssetTransaction.builder()
                .assetTransactionIdentifier(new AssetTransactionIdentifier())
                .assetModel(assetModel2)
                .paymentModel(paymentModel2)
                .userModel(userModel2)
                .status(AssetTransactionStatus.PENDING)
                .type(AssetTransactionType.SUBSCRIPTION)
                .discount(new Discount(new BigDecimal("0.15"), new BigDecimal("11.99"), "New Year Discount"))
                .build();

        assetTransactionRepository.save(assetTransaction1);
        assetTransactionRepository.save(assetTransaction2);
    }

    @Test
    void testPurchaseIsSaved() throws InterruptedException {
        Thread.sleep(500);
        var result = assetTransactionRepository.findById(assetTransaction1.getId());
        assertTrue(result.isPresent());
        assertEquals(
                assetTransaction1.getAssetTransactionIdentifier().getAssetTransactionId(),
                result.get().getAssetTransactionIdentifier().getAssetTransactionId()
        );
    }

    @Test
    void testFindByAssetTransactionIdentifierReturnsCorrectEntity() {
        var id = assetTransaction2.getAssetTransactionIdentifier().getAssetTransactionId();
        AssetTransaction found = assetTransactionRepository.findByAssetTransactionIdentifier_AssetTransactionId(id);
        assertNotNull(found, "Expected to find transaction by its public identifier");
        assertEquals(
                id,
                found.getAssetTransactionIdentifier().getAssetTransactionId(),
                "The returned transaction's identifier should match"
        );
    }

    @Test
    void testExistsByUserModelAndAssetModelReturnsTrue() {
        String userId = assetTransaction1.getUserModel().getUserId();
        String assetId = assetTransaction1.getAssetModel().getAssetId();
        assertTrue(
                assetTransactionRepository.existsByUserModel_UserIdAndAssetModel_AssetId(userId, assetId),
                "Repository should report that a transaction exists for this user and asset"
        );
    }

    @Test
    void testExistsByUserModelAndAssetModelReturnsFalse() {
        // Pick IDs that were not saved
        assertFalse(
                assetTransactionRepository.existsByUserModel_UserIdAndAssetModel_AssetId("non-existent-user", "non-existent-asset"),
                "Repository should report no transaction exists for unknown user and asset"
        );
    }

    @Test
    void testAssetTransactionIdentifierParameterizedConstructor() {
        String fixedId = "fixed-transaction-id-000";
        AssetTransactionIdentifier identifier = new AssetTransactionIdentifier(fixedId);

        assertEquals(fixedId, identifier.getAssetTransactionId(),
                "Parameterized constructor should set the given assetTransactionId");
    }

    @Test
    void testAssetTransactionAllArgsConstructorSetsAllFields() {
        // prepare models
        UserModel user = UserModel.builder()
                .userId("u-1").username("user1")
                .emailAddress(new EmailAddress("u1@example.com"))
                .phoneNumber(new PhoneNumber("123"))
                .build();

        AssetModel asset = AssetModel.builder()
                .assetId("a-1")
                .name("Asset1")
                .assetType(AssetType.MODEL)
                .licenseType(LicenseType.PERSONAL_USE)
                .build();

        PaymentModel payment = PaymentModel.builder()
                .paymentId("p-1")
                .price(new BigDecimal("9.99"))
                .currency(Currency.CAD)
                .paymentType(PaymentType.PAYPAL)
                .transactionStatus(TransactionStatus.PENDING)
                .build();

        Discount discount = new Discount(
                new BigDecimal("0.20"),
                new BigDecimal("2.00"),
                "Test discount"
        );

        // use the six-arg constructor
        AssetTransaction tx = new AssetTransaction(
                user, payment, asset,
                AssetTransactionStatus.PAID,
                AssetTransactionType.SINGLE_ASSET,
                discount
        );

        // identifier should be auto-generated, not null or empty
        assertNotNull(tx.getAssetTransactionIdentifier(), "Identifier object must be non-null");
        assertNotNull(tx.getAssetTransactionIdentifier().getAssetTransactionId(),
                "Identifier string must be non-null");
        assertFalse(tx.getAssetTransactionIdentifier().getAssetTransactionId().isBlank(),
                "Identifier string must not be blank");

        // all other fields should match
        assertSame(user, tx.getUserModel(), "UserModel must be set");
        assertSame(payment, tx.getPaymentModel(), "PaymentModel must be set");
        assertSame(asset, tx.getAssetModel(), "AssetModel must be set");
        assertEquals(AssetTransactionStatus.PAID, tx.getStatus(), "Status must be PAID");
        assertEquals(AssetTransactionType.SINGLE_ASSET, tx.getType(), "Type must be SINGLE_ASSET");
        assertSame(discount, tx.getDiscount(), "Discount must be set");
    }
}
