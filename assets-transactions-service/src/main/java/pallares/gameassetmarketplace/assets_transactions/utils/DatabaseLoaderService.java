package pallares.gameassetmarketplace.assets_transactions.utils;

import pallares.gameassetmarketplace.assets_transactions.dataAccessLayer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.AssetModel;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.AssetType;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.assets.LicenseType;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.payments.*;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users.EmailAddress;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users.PhoneNumber;
import pallares.gameassetmarketplace.assets_transactions.domainClientLayer.users.UserModel;

import java.math.BigDecimal;

@Component
public class DatabaseLoaderService implements CommandLineRunner {

    @Autowired
    AssetTransactionRepository assetTransactionRepository;

    @Override
    public void run(String... args) throws Exception {
        var assetTransactionIdentifier1 = new AssetTransactionIdentifier();
        AssetModel assetModel1 = AssetModel.builder()
                .assetId("c6651849-c406-4563-a937-a441d380ff25")
                .name("Sci-Fi Spaceship")
                .description("A high-quality 3D spaceship model.")
                .assetType(AssetType.MODEL)
                .fileUrl("https://example.com/files/spaceship.obj")
                .thumbnailUrl("https://example.com/thumbnails/spaceship.jpg")
                .licenseType(LicenseType.ROYALTY_FREE)
                .build();

        var paymentModel1 = PaymentModel.builder()
                .paymentId("550e8400-e29b-41d4-a716-446655440000")
                .price(new BigDecimal("49.99"))
                .currency(Currency.US)
                .paymentType(PaymentType.CREDIT_CARD)
                .transactionStatus(TransactionStatus.COMPLETED)
                .build();

        var userModel = UserModel.builder()
                .userId("c5440a89-cb47-4d96-888e-yy96708db4d9")
                .username("AU")
                .emailAddress(new EmailAddress("aucceli0@dot.gov"))
                .phoneNumber(new PhoneNumber("526-461-4430"))
                .build();

        var assetTransaction1 = AssetTransaction.builder()
                .assetTransactionIdentifier(assetTransactionIdentifier1)
                .userModel(userModel)
                .paymentModel(paymentModel1)
                .assetModel(assetModel1)
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(new BigDecimal("0.10"), new BigDecimal("4.99"), "Boxing Day Discount"))
                .build();

        assetTransactionRepository.save(assetTransaction1);


        var assetTransactionIdentifier2 = new AssetTransactionIdentifier();
        var assetModel2 = AssetModel.builder()
                .assetId("c5e6a90f-0af4-44b3-bc7d-1d92f09cbf41")
                .name("Horror Sound FX Pack")
                .description("Creepy sound effects for horror games.")
                .assetType(AssetType.AUDIO)
                .fileUrl("https://example.com/files/horror_sounds.zip")
                .thumbnailUrl("https://example.com/thumbnails/horror_sounds.jpg")
                .licenseType(LicenseType.PERSONAL_USE)
                .build();

        var paymentModel2 = PaymentModel.builder()
                .paymentId("54c446c9-ebfc-4f2e-93ca-d29cfd7a3840")
                .price(new BigDecimal("47.75"))
                .currency(Currency.CAD)
                .paymentType(PaymentType.CREDIT_CARD)
                .transactionStatus(TransactionStatus.COMPLETED)
                .build();

        var assetTransaction2 = AssetTransaction.builder()
                .assetTransactionIdentifier(assetTransactionIdentifier2)
                .userModel(userModel)
                .paymentModel(paymentModel2)
                .assetModel(assetModel2)
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SUBSCRIPTION)
                .discount(new Discount(new BigDecimal("0.10"), new BigDecimal("4.77"), "Boxing Day Discount"))
                .build();

        assetTransactionRepository.save(assetTransaction2);
    }
}
