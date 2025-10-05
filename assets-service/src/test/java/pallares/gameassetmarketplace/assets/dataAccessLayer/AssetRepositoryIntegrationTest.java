package pallares.gameassetmarketplace.assets.dataAccessLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pallares.gameassetmarketplace.assets.utils.exceptions.AssetFileMissingException;
import pallares.gameassetmarketplace.assets.utils.exceptions.InvalidAssetPriceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AssetRepositoryIntegrationTest {
    @Autowired
    private AssetRepository assetRepository;

    @BeforeEach
    public void setUp() {assetRepository.deleteAll();}

    @Test
    public void whenAssetsExists_thenReturnAllAssets() {
        //arrange
        BigDecimal price = new BigDecimal("32.99");
        LocalDate creationDate = LocalDate.of(2018, 1, 1);
        LocalDate updatedDate = LocalDate.of(2020, 4, 2);

        LocalDate creationDate2 = LocalDate.of(2021, 3, 10);
        LocalDate updatedDate2 = LocalDate.of(2021, 8, 26);
        Asset asset1 = new Asset("Monster-Bear", "A monster bear to use as a mob", AssetType.MODEL,
                price, "https://example.com/files/monsterBear.obj", "https://example.com/thumbnails/monsterBear.jpg",
                LicenseType.ROYALTY_FREE, creationDate, updatedDate);

        Asset asset2 = new Asset("Monster-Dragon", "A monster dragon to use as a mob", AssetType.MODEL,
                price, "https://example.com/files/monsterDragon.obj", "https://example.com/thumbnails/monsterDragon.jpg",
                LicenseType.ROYALTY_FREE, creationDate2, updatedDate2);
        assetRepository.save(asset1);
        assetRepository.save(asset2);
        long afterSizeDB = assetRepository.count();

        //act
        List<Asset> assets = assetRepository.findAll();

        //assert
        assertNotNull(assets);
        assertNotEquals(0, afterSizeDB);
        assertEquals(afterSizeDB, assets.size());
    }

    @Test
    public void whenAssetExists_thenReturnAssetByAssetId() {
        //arrange
        BigDecimal price = new BigDecimal("32.99");
        LocalDate creationDate = LocalDate.of(2018, 1, 1);
        LocalDate updatedDate = LocalDate.of(2020, 4, 2);

        LocalDate creationDate2 = LocalDate.of(2021, 3, 10);
        LocalDate updatedDate2 = LocalDate.of(2021, 8, 26);
        Asset asset1 = new Asset("Monster-Bear", "A monster bear to use as a mob", AssetType.MODEL,
                price, "https://example.com/files/monsterBear.obj", "https://example.com/thumbnails/monsterBear.jpg",
                LicenseType.ROYALTY_FREE, creationDate, updatedDate
        );

        Asset asset2 = new Asset("Monster-Dragon", "A monster dragon to use as a mob", AssetType.MODEL,
                price, "https://example.com/files/monsterDragon.obj", "https://example.com/thumbnails/monsterDragon.jpg",
                LicenseType.ROYALTY_FREE, creationDate2, updatedDate2
        );
        assetRepository.save(asset1);
        assetRepository.save(asset2);

        //act
        Asset foundAsset = assetRepository.findByAssetIdentifier_AssetId(asset1.getAssetIdentifier().getAssetId());

        //assert
        assertNotNull(foundAsset);
        assertEquals(asset1.getAssetIdentifier().getAssetId(), foundAsset.getAssetIdentifier().getAssetId());
        assertEquals(asset1.getName(), foundAsset.getName());
        assertEquals(asset1.getAssetType(), foundAsset.getAssetType());
    }

    @Test
    public void whenAssetDoesNotExist_thenReturnEmpty() {
        //arrange
        final String NOT_FOUND_ASSET_ID = "c6651849-c406-4563-a937-a441d380ff25";

        //act
        Asset foundAsset = assetRepository.findByAssetIdentifier_AssetId(NOT_FOUND_ASSET_ID);

        //assert
        assertNull(foundAsset);
    }

    @Test
    public void whenAssetEntityIsValid_thenAddAsset() {
        //arrange
        BigDecimal price = new BigDecimal("44.99");
        LocalDate creationDate = LocalDate.of(2016, 12, 14);
        LocalDate updatedDate = LocalDate.of(2024, 5, 22);

        Asset asset = new Asset("Character Jump", "Code to make your character jump", AssetType.SCRIPT,
                price, "https://example.com/files/characterJump.obj", "https://example.com/thumbnails/characterJump.jpg",
                LicenseType.ROYALTY_FREE, creationDate, updatedDate
        );

        //act
        Asset savedAsset = assetRepository.save(asset);

        //assert
        assertNotNull(savedAsset);
        assertNotNull(savedAsset.getId());
        assertNotNull(savedAsset.getAssetIdentifier());
        assertNotNull(savedAsset.getAssetIdentifier().getAssetId());
        assertEquals(asset.getName(), savedAsset.getName());
        assertEquals(asset.getDescription(), savedAsset.getDescription());
        assertEquals(asset.getAssetType(), savedAsset.getAssetType());
        assertEquals(asset.getPrice(), savedAsset.getPrice());
        assertEquals(asset.getFileUrl(), savedAsset.getFileUrl());
        assertEquals(asset.getThumbnailUrl(), savedAsset.getThumbnailUrl());
        assertEquals(asset.getLicenseType(), savedAsset.getLicenseType());
        assertEquals(asset.getCreatedDate(), savedAsset.getCreatedDate());
        assertEquals(asset.getUpdatedDate(), savedAsset.getUpdatedDate());
    }

    @Test
    public void whenAssetIsValid_thenUpdateAsset() {
        // arrange
        BigDecimal price = new BigDecimal("76.99");
        LocalDate creationDate = LocalDate.of(2022, 3, 6);
        LocalDate updatedDate = LocalDate.of(2025, 2, 24);

        Asset asset = new Asset("Character Dodge", "Code to make your character dodge", AssetType.SCRIPT,
                price, "https://example.com/files/characterdodge.obj", "https://example.com/thumbnails/characterdodge.jpg",
                LicenseType.ROYALTY_FREE, creationDate, updatedDate
        );
        Asset savedAsset = assetRepository.save(asset);

        // act
        savedAsset.setName("Ultra Instinct");
        savedAsset.setLicenseType(LicenseType.COMMERCIAL);

        Asset updatedAsset = assetRepository.save(savedAsset);

        // assert
        Asset foundAsset = assetRepository.findByAssetIdentifier_AssetId(updatedAsset.getAssetIdentifier().getAssetId());
        assertNotNull(foundAsset);
        assertEquals("Ultra Instinct", foundAsset.getName());
        assertEquals(LicenseType.COMMERCIAL, foundAsset.getLicenseType());
    }


    @Test
    public void whenAssetIsValid_thenDeleteAsset() {
        // arrange
        BigDecimal price = new BigDecimal("99.99");
        LocalDate creationDate = LocalDate.of(2013, 6, 7);
        LocalDate updatedDate = LocalDate.of(2019, 8, 25);

        Asset asset = new Asset("Character Block", "Code to make your character block", AssetType.SCRIPT,
                price, "https://example.com/files/characterblock.obj", "https://example.com/thumbnails/characterblock.jpg",
                LicenseType.ROYALTY_FREE, creationDate, updatedDate
        );
        Asset savedAsset = assetRepository.save(asset);
        String assetId = savedAsset.getAssetIdentifier().getAssetId();

        // act
        assetRepository.delete(savedAsset);

        // assert
        Asset deletedAsset = assetRepository.findByAssetIdentifier_AssetId(assetId);
        assertNull(deletedAsset);
        assertEquals(0, assetRepository.count());
    }

    @Test
    public void whenSavingTwoAssets_thenAssetIdentifiersAreUnique() {
        // Arrange
        Asset asset1 = new Asset("Asset1", "desc", AssetType.SCRIPT,
                new BigDecimal("10.00"), "http://f1", "http://t1",
                LicenseType.COMMERCIAL, LocalDate.now(), LocalDate.now());

        Asset asset2 = new Asset("Asset2", "desc", AssetType.SCRIPT,
                new BigDecimal("12.00"), "http://f2", "http://t2",
                LicenseType.ROYALTY_FREE, LocalDate.now(), LocalDate.now());

        // Act
        asset1 = assetRepository.save(asset1);
        asset2 = assetRepository.save(asset2);

        // Assert
        assertNotEquals(asset1.getAssetIdentifier().getAssetId(), asset2.getAssetIdentifier().getAssetId());
    }

}