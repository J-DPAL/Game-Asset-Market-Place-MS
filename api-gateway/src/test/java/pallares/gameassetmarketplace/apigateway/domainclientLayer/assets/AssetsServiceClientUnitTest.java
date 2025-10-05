package pallares.gameassetmarketplace.apigateway.domainclientLayer.assets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assets.AssetRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.assets.AssetResponseModel;
import pallares.gameassetmarketplace.apigateway.utils.HttpErrorInfo;
import pallares.gameassetmarketplace.apigateway.utils.exceptions.InvalidInputException;
import pallares.gameassetmarketplace.apigateway.utils.exceptions.NotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetsServiceClientUnitTest {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private AssetsServiceClient assetsServiceClient;

    private final String baseUrl = "http://localhost:8081/api/v1/assets";
    private final String assetId = "c6651849-c406-4563-a937-a441d380ff25";

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);

        // <<< REGISTER THE JSR-310 MODULE >>>
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())                     // support java.time types
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // use ISO strings

        assetsServiceClient = new AssetsServiceClient(restTemplate, objectMapper, "localhost", "8081");
    }

    private AssetResponseModel sampleResponse() {
        return new AssetResponseModel(
                assetId,
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
    @DisplayName("getAssetByAssetId returns AssetResponseModel on success")
    void testGetAssetByAssetId_success() {
        when(restTemplate.getForObject(baseUrl + "/" + assetId, AssetResponseModel.class))
                .thenReturn(sampleResponse());

        AssetResponseModel result = assetsServiceClient.getAssetByAssetId(assetId);

        assertThat(result).isNotNull();
        assertThat(result.getAssetId()).isEqualTo(assetId);
    }

    @Test
    @DisplayName("getAssets returns list of assets on success")
    void testGetAssets_success() {
        AssetResponseModel[] responseArray = new AssetResponseModel[]{sampleResponse()};
        when(restTemplate.getForEntity(baseUrl, AssetResponseModel[].class))
                .thenReturn(new ResponseEntity<>(responseArray, HttpStatus.OK));

        List<AssetResponseModel> result = assetsServiceClient.getAssets();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAssetId()).isEqualTo(assetId);
    }

    @Test
    @DisplayName("addAsset returns created AssetResponseModel")
    void testAddAsset_success() {
        AssetRequestModel request = sampleRequest();
        when(restTemplate.postForObject(baseUrl, request, AssetResponseModel.class))
                .thenReturn(sampleResponse());

        AssetResponseModel result = assetsServiceClient.addAsset(request);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Spaceship");
    }

    @Test
    @DisplayName("updateAsset should call put without exception")
    void testUpdateAsset_success() {
        AssetRequestModel request = sampleRequest();
        doNothing().when(restTemplate).put(baseUrl + "/" + assetId, request);

        assetsServiceClient.updateAsset(assetId, request);

        verify(restTemplate).put(baseUrl + "/" + assetId, request);
    }

    @Test
    @DisplayName("removeAsset should call delete without exception")
    void testRemoveAsset_success() {
        doNothing().when(restTemplate).delete(baseUrl + "/" + assetId);

        assetsServiceClient.removeAsset(assetId);

        verify(restTemplate).delete(baseUrl + "/" + assetId);
    }

    @Test
    @DisplayName("getAssetByAssetId should throw NotFoundException")
    void testGetAssetByAssetId_notFound() throws Exception {
        HttpErrorInfo errorInfo = new HttpErrorInfo(HttpStatus.NOT_FOUND, "/api/v1/assets/" + assetId, "Asset not found");
        String errorBody = objectMapper.writeValueAsString(errorInfo);
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.NOT_FOUND, "Not Found", HttpHeaders.EMPTY, errorBody.getBytes(), null
        );

        when(restTemplate.getForObject(baseUrl + "/" + assetId, AssetResponseModel.class)).thenThrow(ex);

        assertThatThrownBy(() -> assetsServiceClient.getAssetByAssetId(assetId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Asset not found");
    }

    @Test
    @DisplayName("addAsset should throw InvalidInputException on 422")
    void testAddAsset_invalidInput() throws Exception {
        HttpErrorInfo errorInfo = new HttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, baseUrl, "Invalid asset input");
        String errorBody = objectMapper.writeValueAsString(errorInfo);
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable", HttpHeaders.EMPTY, errorBody.getBytes(), null
        );

        when(restTemplate.postForObject(eq(baseUrl), any(), eq(AssetResponseModel.class)))
                .thenThrow(ex);

        assertThatThrownBy(() -> assetsServiceClient.addAsset(sampleRequest()))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Invalid asset input");
    }
}
