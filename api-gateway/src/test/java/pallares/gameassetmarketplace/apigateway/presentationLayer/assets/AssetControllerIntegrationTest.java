package pallares.gameassetmarketplace.apigateway.presentationLayer.assets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import pallares.gameassetmarketplace.apigateway.businessLayer.assets.AssetService;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assets.AssetType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assets.LicenseType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@WebFluxTest(controllers = AssetController.class)
class AssetControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AssetService assetService;

    private AssetResponseModel mockAsset;

    @BeforeEach
    void setUp() {
        mockAsset = new AssetResponseModel(
                "asset123",
                "Sample Asset",
                "Test description",
                AssetType.IMAGE,
                new BigDecimal("29.99"),
                "http://example.com/file.png",
                "http://example.com/thumb.png",
                LicenseType.PERSONAL_USE,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2)
        );
    }

    @Test
    void getAssetByAssetId_ReturnsAsset() {
        when(assetService.getAssetByAssetId("asset123")).thenReturn(mockAsset);

        webTestClient.get()
                .uri("/api/v1/assets/asset123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.assetId").isEqualTo("asset123")
                .jsonPath("$.name").isEqualTo("Sample Asset");

        verify(assetService, times(1)).getAssetByAssetId("asset123");
    }

    @Test
    void getAllAssets_ReturnsListOfAssets() {
        when(assetService.getAllAssets()).thenReturn(List.of(mockAsset));

        webTestClient.get()
                .uri("/api/v1/assets")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AssetResponseModel.class).hasSize(1);

        verify(assetService, times(1)).getAllAssets();
    }

    @Test
    void addAsset_ReturnsCreatedAsset() {
        AssetRequestModel requestModel = new AssetRequestModel(
                "New Asset",
                "Description",
                AssetType.MODEL,
                new BigDecimal("49.99"),
                "http://example.com/model.obj",
                "http://example.com/thumb.jpg",
                LicenseType.ROYALTY_FREE,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 2)
        );

        when(assetService.addAsset(requestModel)).thenReturn(mockAsset);

        webTestClient.post()
                .uri("/api/v1/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.assetId").isEqualTo("asset123");

        verify(assetService, times(1)).addAsset(requestModel);
    }

    @Test
    void updateAsset_ReturnsUpdatedAsset() {
        AssetRequestModel updateModel = new AssetRequestModel(
                "Updated Asset",
                "Updated Desc",
                AssetType.AUDIO,
                new BigDecimal("9.99"),
                "http://example.com/sound.mp3",
                "http://example.com/thumb.png",
                LicenseType.PERSONAL_USE,
                LocalDate.of(2023, 10, 10),
                LocalDate.of(2023, 11, 11)
        );

        when(assetService.updateAsset(updateModel, "asset123")).thenReturn(mockAsset);

        webTestClient.put()
                .uri("/api/v1/assets/asset123")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateModel)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.assetId").isEqualTo("asset123");

        verify(assetService).updateAsset(updateModel, "asset123");
    }

    @Test
    void deleteAsset_ReturnsNoContent() {
        doNothing().when(assetService).removeAsset("asset123");

        webTestClient.delete()
                .uri("/api/v1/assets/asset123")
                .exchange()
                .expectStatus().isNoContent();

        verify(assetService).removeAsset("asset123");
    }
}
