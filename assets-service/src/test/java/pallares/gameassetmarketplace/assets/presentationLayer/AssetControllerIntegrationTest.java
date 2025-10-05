package pallares.gameassetmarketplace.assets.presentationLayer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import pallares.gameassetmarketplace.assets.dataAccessLayer.AssetRepository;
import pallares.gameassetmarketplace.assets.dataAccessLayer.AssetType;
import pallares.gameassetmarketplace.assets.dataAccessLayer.LicenseType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("h2")
@Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AssetControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AssetRepository assetRepository;

    private final String BASE_URL_ASSETS = "/api/v1/assets";
    private final String VALID_ASSET_UUID = "c6651849-c406-4563-a937-a441d380ff25";
    private final String INVALID_ASSET_UUID = "c6651849-c406-4563-a937-a441d380ff2";
    private final String NOTFOUND_ASSET_UUID = "c6651849-c406-4563-a937-a441d380ff24";

    @Test
    public void whenAssetsExist_thenReturnAllAssets() {
        long sizeDb = assetRepository.count();

        webTestClient.get()
                .uri(BASE_URL_ASSETS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(AssetResponseModel.class)
                .value(list -> {
                    assertNotNull(list);
//                    assertNotEquals(sizeDb, list.size());
                    assertEquals(sizeDb, list.size());
                });
    }

    @Test
    public void whenAssetRequestIsValid_thenCreateAndReturnAsset() {
        AssetRequestModel assetRequestModel = AssetRequestModel.builder()
                .name("Test Asset")
                .description("This is a test asset.")
                .assetType(AssetType.IMAGE)
                .price(new BigDecimal("9.99"))
                .fileUrl("http://example.com/file.png")
                .thumbnailUrl("http://example.com/thumb.png")
                .licenseType(LicenseType.COMMERCIAL)
                .createdDate(LocalDate.now())
                .updatedDate(LocalDate.now())
                .build();

        webTestClient.post()
                .uri(BASE_URL_ASSETS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(assetRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(AssetResponseModel.class)
                .value(assetResponseModel -> {
                    assertNotNull(assetResponseModel);
                    assertEquals(assetRequestModel.getName(), assetResponseModel.getName());
                });
    }

    @Test
    public void whenAssetIdIsValid_thenReturnAsset() {
        webTestClient.get()
                .uri(BASE_URL_ASSETS + "/" + VALID_ASSET_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.assetId").isEqualTo(VALID_ASSET_UUID);
    }

    @Test
    public void whenAssetIdIsInvalid_thenReturnUnprocessableEntity() {
        webTestClient.get()
                .uri(BASE_URL_ASSETS + "/" + INVALID_ASSET_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid assetIdentifier provided: " + INVALID_ASSET_UUID);
    }

    @Test
    public void whenAssetExistsOnDelete_thenReturnNoContent() {
        webTestClient.delete()
                .uri(BASE_URL_ASSETS + "/" + VALID_ASSET_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri(BASE_URL_ASSETS + "/" + VALID_ASSET_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Provided assetIdentifier not found: " + VALID_ASSET_UUID);
    }

    @Test
    public void whenAssetRequestIsMissingFields_thenReturnBadRequest() {
        // Arrange
        AssetRequestModel assetRequestModel = AssetRequestModel.builder()
                .name(null)
                .description(null)
                .assetType(null)
                .price(null)
                .fileUrl(null)
                .thumbnailUrl(null)
                .licenseType(null)
                .createdDate(null)
                .updatedDate(null)
                .build();

        // Act + Assert
        webTestClient.post()
                .uri(BASE_URL_ASSETS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(assetRequestModel)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(AssetResponseModel.class)
                .value(assetResponseModel -> {
                    assertNotNull(assetResponseModel);
                    assertNull(assetResponseModel.getName());
                    assertNull(assetResponseModel.getDescription());
                    assertNull(assetResponseModel.getAssetType());
                    assertNull(assetResponseModel.getPrice());
                });
    }

    @Test
    public void whenAssetIsUpdated_thenReturnUpdatedAsset() {
        // Arrange
        AssetRequestModel updateRequest = AssetRequestModel.builder()
                .name("Updated Name")
                .description("Updated Description")
                .assetType(AssetType.MODEL)
                .price(new BigDecimal("29.99"))
                .fileUrl("http://updated.com/file.obj")
                .thumbnailUrl("http://updated.com/thumb.jpg")
                .licenseType(LicenseType.ROYALTY_FREE)
                .createdDate(LocalDate.now())
                .updatedDate(LocalDate.now())
                .build();

        // Act + Assert
        webTestClient.put()
                .uri(BASE_URL_ASSETS + "/" + VALID_ASSET_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Updated Name")
                .jsonPath("$.price").isEqualTo(29.99);
    }

    @Test
    public void whenDeletingNonExistentAsset_thenReturnNotFound() {
        // Act + Assert
        webTestClient.delete()
                .uri(BASE_URL_ASSETS + "/" + NOTFOUND_ASSET_UUID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Provided assetIdentifier not found: " + NOTFOUND_ASSET_UUID);
    }

}
