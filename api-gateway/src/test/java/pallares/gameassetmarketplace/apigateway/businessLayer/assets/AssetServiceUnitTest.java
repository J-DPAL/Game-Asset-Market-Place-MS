package pallares.gameassetmarketplace.apigateway.businessLayer.assets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assets.AssetsServiceClient;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assets.AssetType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assets.LicenseType;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assets.AssetRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assets.AssetResponseModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AssetServiceUnitTest {

    private AssetsServiceClient assetsServiceClient;
    private AssetServiceImpl assetService;

    private final String ASSET_ID = "c6651849-c406-4563-a937-a441d380ff25";

    @BeforeEach
    void setUp() {
        assetsServiceClient = mock(AssetsServiceClient.class);
        assetService = new AssetServiceImpl(assetsServiceClient);
    }

    private AssetRequestModel sampleRequest() {
        return new AssetRequestModel(
                "Spaceship",
                "3D model",
                AssetType.MODEL,
                new BigDecimal("49.99"),
                "https://example.com/files/spaceship.obj",
                "https://example.com/thumbnails/spaceship.jpg",
                LicenseType.ROYALTY_FREE,
                LocalDate.now().minusDays(1),
                LocalDate.now()
        );
    }

    private AssetResponseModel sampleResponse() {
        return new AssetResponseModel(
                ASSET_ID,
                "Spaceship",
                "3D model",
                AssetType.MODEL,
                new BigDecimal("49.99"),
                "https://example.com/files/spaceship.obj",
                "https://example.com/thumbnails/spaceship.jpg",
                LicenseType.ROYALTY_FREE,
                LocalDate.now().minusDays(1),
                LocalDate.now()
        );
    }

    @Test
    @DisplayName("getAssetByAssetId should return asset")
    void testGetAssetById() {
        var expected = sampleResponse();
        when(assetsServiceClient.getAssetByAssetId(ASSET_ID)).thenReturn(expected);

        var result = assetService.getAssetByAssetId(ASSET_ID);

        assertThat(result).isEqualTo(expected);
        verify(assetsServiceClient).getAssetByAssetId(ASSET_ID);
    }

    @Test
    @DisplayName("getAllAssets should return list of assets")
    void testGetAllAssets() {
        var expected = List.of(sampleResponse());
        when(assetsServiceClient.getAssets()).thenReturn(expected);

        var result = assetService.getAllAssets();

        assertThat(result).hasSize(1).containsExactlyElementsOf(expected);
        verify(assetsServiceClient).getAssets();
    }

    @Test
    @DisplayName("addAsset should call client and return created asset")
    void testAddAsset() {
        var request = sampleRequest();
        var expected = sampleResponse();
        when(assetsServiceClient.addAsset(request)).thenReturn(expected);

        var result = assetService.addAsset(request);

        assertThat(result).isEqualTo(expected);
        verify(assetsServiceClient).addAsset(request);
    }

    @Test
    @DisplayName("updateAsset should call client update and return updated asset")
    void testUpdateAsset() {
        var request = sampleRequest();
        var expected = sampleResponse();

        doNothing().when(assetsServiceClient).updateAsset(ASSET_ID, request);
        when(assetsServiceClient.getAssetByAssetId(ASSET_ID)).thenReturn(expected);

        var result = assetService.updateAsset(request, ASSET_ID);

        assertThat(result).isEqualTo(expected);
        verify(assetsServiceClient).updateAsset(ASSET_ID, request);
        verify(assetsServiceClient).getAssetByAssetId(ASSET_ID);
    }

    @Test
    @DisplayName("removeAsset should call client remove")
    void testRemoveAsset() {
        doNothing().when(assetsServiceClient).removeAsset(ASSET_ID);

        assetService.removeAsset(ASSET_ID);

        verify(assetsServiceClient).removeAsset(ASSET_ID);
    }
}
