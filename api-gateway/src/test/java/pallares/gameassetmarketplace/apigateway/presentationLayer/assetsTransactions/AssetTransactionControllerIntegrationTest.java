package pallares.gameassetmarketplace.apigateway.presentationLayer.assetsTransactions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pallares.gameassetmarketplace.apigateway.businessLayer.assetsTransactions.AssetTransactionService;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions.AssetTransactionStatus;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions.AssetTransactionType;
import pallares.gameassetmarketplace.apigateway.domainclientLayer.assetsTransactions.Discount;

@WebMvcTest(controllers = AssetTransactionController.class)
class AssetTransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssetTransactionService assetTransactionService;

    @MockitoBean
    private AssetTransactionModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private final String USER_ID = "c5440a89-cb47-4d96-888e-yy96708db4d9";
    private final String TX_ID   = "482227c2-9841-4937-8617-71001450ca16";

    private AssetTransactionResponseModel sampleResponse() {
        return AssetTransactionResponseModel.builder()
                .assetTransactionId(TX_ID)
                .userId(USER_ID)
                .paymentId("550e8400-e29b-41d4-a716-446655440000")
                .assetId("c6651849-c406-4563-a937-a441d380ff25")
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(new BigDecimal("0.10"), new BigDecimal("4.99"), "Boxing Day"))
                .build();
    }

    private AssetTransactionRequestModel sampleRequest() {
        return AssetTransactionRequestModel.builder()
                .assetId("c6651849-c406-4563-a937-a441d380ff25")
                .paymentId("550e8400-e29b-41d4-a716-446655440000")
                .status(AssetTransactionStatus.PAID)
                .type(AssetTransactionType.SINGLE_ASSET)
                .discount(new Discount(new BigDecimal("0.10"), new BigDecimal("4.99"), "Boxing Day"))
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/users/{userId}/asset_transactions → 200")
    void testGetAll() throws Exception {
        var resp = sampleResponse();
        given(assetTransactionService.getAllAssetTransactions(USER_ID))
                .willReturn(List.of(resp));

        mockMvc.perform(get("/api/v1/users/{userId}/asset_transactions", USER_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assetTransactionId").value(TX_ID));
    }

    @Test
    @DisplayName("GET by ID → 200")
    void testGetById() throws Exception {
        var resp = sampleResponse();
        given(assetTransactionService.getAssetTransactionByAssetTransactionId(TX_ID, USER_ID))
                .willReturn(resp);

        mockMvc.perform(get("/api/v1/users/{userId}/asset_transactions/{txId}", USER_ID, TX_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assetTransactionId").value(TX_ID));
    }

    @Test
    @DisplayName("POST create → 201")
    void testCreate() throws Exception {
        var req  = sampleRequest();
        var resp = sampleResponse();
        given(assetTransactionService.addAssetTransaction(any(), eq(USER_ID))).willReturn(resp);

        mockMvc.perform(post("/api/v1/users/{userId}/asset_transactions", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.assetTransactionId").value(TX_ID));
    }

    @Test
    @DisplayName("PUT update → 200")
    void testUpdate() throws Exception {
        var req  = sampleRequest();
        var resp = sampleResponse();
        given(assetTransactionService.updateAssetTransaction(any(), eq(TX_ID), eq(USER_ID))).willReturn(resp);

        mockMvc.perform(put("/api/v1/users/{userId}/asset_transactions/{txId}", USER_ID, TX_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assetTransactionId").value(TX_ID));
    }

    @Test
    @DisplayName("DELETE → 204")
    void testDelete() throws Exception {
        // service.delete just void; no need to stub
        mockMvc.perform(delete("/api/v1/users/{userId}/asset_transactions/{txId}", USER_ID, TX_ID))
                .andExpect(status().isNoContent());
    }
}
