package pallares.gameassetmarketplace.apigateway.domainclientLayer.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pallares.gameassetmarketplace.apigateway.presentationLayer.payments.PaymentRequestModel;
import pallares.gameassetmarketplace.apigateway.presentationLayer.payments.PaymentResponseModel;
import pallares.gameassetmarketplace.apigateway.utils.HttpErrorInfo;
import pallares.gameassetmarketplace.apigateway.utils.exceptions.InvalidInputException;
import pallares.gameassetmarketplace.apigateway.utils.exceptions.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PaymentsServiceClientUnitTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private PaymentsServiceClient client;

    private final String host = "host";
    private final String port = "8080";
    private String baseUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new PaymentsServiceClient(restTemplate, mapper, host, port);
        baseUrl = "http://" + host + ":" + port + "/api/v1/payments";
    }

    @Test
    void getPaymentByPaymentId_success() {
        PaymentResponseModel expected = new PaymentResponseModel(
                "pid", new BigDecimal("10.00"), Currency.US, PaymentType.CREDIT_CARD, TransactionStatus.COMPLETED
        );
        when(restTemplate.getForObject(baseUrl + "/pid", PaymentResponseModel.class))
                .thenReturn(expected);

        var actual = client.getPaymentByPaymentId("pid");

        assertEquals(expected.getPaymentId(), actual.getPaymentId());
        assertEquals(expected.getPrice(), actual.getPrice());
        verify(restTemplate).getForObject(baseUrl + "/pid", PaymentResponseModel.class);
    }

    @Test
    void getPaymentByPaymentId_notFound_throwsNotFoundException() throws Exception {
        HttpClientErrorException ex = mock(HttpClientErrorException.class);
        when(ex.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        when(ex.getResponseBodyAsString()).thenReturn("{\"message\":\"not found\"}");
        when(mapper.readValue("{\"message\":\"not found\"}", HttpErrorInfo.class))
                .thenReturn(new HttpErrorInfo(HttpStatus.NOT_FOUND, "/path", "not found"));
        when(restTemplate.getForObject(anyString(), eq(PaymentResponseModel.class)))
                .thenThrow(ex);

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> client.getPaymentByPaymentId("missing"));
        assertEquals("not found", thrown.getMessage());
    }

    @Test
    void getPayments_success() {
        PaymentResponseModel[] payload = {
                new PaymentResponseModel("p1", new BigDecimal("1.00"), Currency.EUR, PaymentType.PAYPAL, TransactionStatus.PENDING),
                new PaymentResponseModel("p2", new BigDecimal("2.00"), Currency.US, PaymentType.CREDIT_CARD, TransactionStatus.COMPLETED)
        };
        ResponseEntity<PaymentResponseModel[]> response = ResponseEntity.ok(payload);
        when(restTemplate.getForEntity(baseUrl, PaymentResponseModel[].class)).thenReturn(response);

        List<PaymentResponseModel> list = client.getPayments();

        assertEquals(2, list.size());
        assertEquals("p1", list.get(0).getPaymentId());
        verify(restTemplate).getForEntity(baseUrl, PaymentResponseModel[].class);
    }

    @Test
    void addPayment_success() {
        PaymentRequestModel req = new PaymentRequestModel(
                new BigDecimal("5.00"), Currency.CAD, PaymentType.PAYPAL, TransactionStatus.PENDING
        );
        PaymentResponseModel resp = new PaymentResponseModel(
                "newid", new BigDecimal("5.00"), Currency.CAD, PaymentType.PAYPAL, TransactionStatus.PENDING
        );
        when(restTemplate.postForObject(baseUrl, req, PaymentResponseModel.class)).thenReturn(resp);

        var actual = client.addPayment(req);

        assertEquals("newid", actual.getPaymentId());
        verify(restTemplate).postForObject(baseUrl, req, PaymentResponseModel.class);
    }

    @Test
    void updatePayment_success() {
        PaymentRequestModel req = new PaymentRequestModel(
                new BigDecimal("0.00"), Currency.US, PaymentType.CREDIT_CARD, TransactionStatus.COMPLETED
        );

        assertDoesNotThrow(() -> client.updatePayment("u1", req));

        verify(restTemplate).put(eq(baseUrl + "/u1"), eq(req));
    }


    @Test
    void removePayment_success() {
        assertDoesNotThrow(() -> client.removePayment("r1"));
        verify(restTemplate).delete(baseUrl + "/r1");
    }

    @Test
    void addPayment_unprocessableEntity_throwsInvalidInputException() throws Exception {
        HttpClientErrorException ex = mock(HttpClientErrorException.class);
        when(ex.getStatusCode()).thenReturn(HttpStatus.UNPROCESSABLE_ENTITY);
        when(ex.getResponseBodyAsString()).thenReturn("{\"message\":\"bad data\"}");
        when(mapper.readValue("{\"message\":\"bad data\"}", HttpErrorInfo.class))
                .thenReturn(new HttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, "/path", "bad data"));
        when(restTemplate.postForObject(anyString(), any(), eq(PaymentResponseModel.class)))
                .thenThrow(ex);

        InvalidInputException thrown = assertThrows(InvalidInputException.class,
                () -> client.addPayment(new PaymentRequestModel(
                        new BigDecimal("1.00"), Currency.US, PaymentType.CREDIT_CARD, TransactionStatus.COMPLETED
                ))
        );
        assertEquals("bad data", thrown.getMessage());
    }
}
