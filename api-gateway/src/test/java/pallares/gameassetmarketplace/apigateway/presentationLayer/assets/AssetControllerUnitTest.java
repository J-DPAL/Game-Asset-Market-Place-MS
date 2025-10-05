package pallares.gameassetmarketplace.apigateway.presentationLayer.assets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pallares.gameassetmarketplace.apigateway.businessLayer.assets.AssetService;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assets.AssetType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assets.LicenseType;

@WebMvcTest(controllers = AssetController.class)
class AssetControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssetService assetService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String ASSET_ID = "c6651849-c406-4563-a937-a441d380ff25";

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

    @Test
    @DisplayName("GET /api/v1/assets/{assetId} → 200")
    void getAssetById() throws Exception {
        var resp = sampleResponse();
        given(assetService.getAssetByAssetId(ASSET_ID)).willReturn(resp);

        mockMvc.perform(get("/api/v1/assets/{assetId}", ASSET_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assetId").value(ASSET_ID))
                .andExpect(jsonPath("$.name").value("Spaceship"));
    }

    @Test
    @DisplayName("GET /api/v1/assets → 200")
    void getAllAssets() throws Exception {
        var resp = sampleResponse();
        given(assetService.getAllAssets()).willReturn(List.of(resp));

        mockMvc.perform(get("/api/v1/assets")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assetId").value(ASSET_ID));
    }

    @Test
    @DisplayName("POST /api/v1/assets → 201")
    void addAsset() throws Exception {
        var req  = sampleRequest();
        var resp = sampleResponse();
        given(assetService.addAsset(any(AssetRequestModel.class))).willReturn(resp);

        mockMvc.perform(post("/api/v1/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.assetId").value(ASSET_ID))
                .andExpect(jsonPath("$.name").value("Spaceship"));
    }

    @Test
    @DisplayName("PUT /api/v1/assets/{assetId} → 200")
    void updateAsset() throws Exception {
        var req  = sampleRequest();
        var resp = sampleResponse();
        given(assetService.updateAsset(any(AssetRequestModel.class), eq(ASSET_ID))).willReturn(resp);

        mockMvc.perform(put("/api/v1/assets/{assetId}", ASSET_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assetId").value(ASSET_ID));
    }

    @Test
    @DisplayName("DELETE /api/v1/assets/{assetId} → 204")
    void removeAsset() throws Exception {
        // assetService.removeAsset is void
        mockMvc.perform(delete("/api/v1/assets/{assetId}", ASSET_ID))
                .andExpect(status().isNoContent());
    }
}
