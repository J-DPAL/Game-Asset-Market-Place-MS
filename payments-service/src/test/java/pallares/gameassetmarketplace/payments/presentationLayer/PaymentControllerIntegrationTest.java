package pallares.gameassetmarketplace.payments.presentationLayer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import pallares.gameassetmarketplace.payments.dataAccessLayer.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("h2")
@Sql(scripts = {"/schema-h2.sql", "/data-h2.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PaymentControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PaymentRepository paymentRepository;

    private final String BASE_URL_PAYMENTS = "/api/v1/payments";
    private final String VALID_PAYMENT_UUID = "550e8400-e29b-41d4-a716-446655440000"; // Adjust if needed
    private final String INVALID_PAYMENT_UUID = "550e8400-e29b-41d4-a716-446655440";
    private final String NOTFOUND_PAYMENT_UUID = "550e8400-e29b-41d4-a716-446655440074";

    @Test
    public void whenPaymentsExist_thenReturnAllPayments() {
        long count = paymentRepository.count();

        webTestClient.get()
                .uri(BASE_URL_PAYMENTS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(PaymentResponseModel.class)
                .value(payments -> {
                    assertNotNull(payments);
                    //assertNotEquals(count, payments.size());
                    assertEquals(count, payments.size());
                });
    }

    @Test
    public void whenValidPaymentRequest_thenCreateAndReturnPayment() {
        ItemPrice itemPrice = new ItemPrice(new BigDecimal("49.99"), Currency.CAD);

        PaymentRequestModel requestModel = PaymentRequestModel.builder()
                .price(itemPrice.getPrice())
                .currency(itemPrice.getCurrency())
                .paymentType(PaymentType.CREDIT_CARD)
                .transactionStatus(TransactionStatus.COMPLETED)
                .build();

        webTestClient.post()
                .uri(BASE_URL_PAYMENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PaymentResponseModel.class)
                .value(paymentResponseModel -> {
                    assertNotNull(paymentResponseModel);
                    assertEquals(requestModel.getPaymentType(), paymentResponseModel.getPaymentType());
                });
    }

    @Test
    public void whenValidPaymentId_thenReturnPayment() {
        webTestClient.get()
                .uri(BASE_URL_PAYMENTS + "/" + VALID_PAYMENT_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.paymentId").isEqualTo(VALID_PAYMENT_UUID);
    }

    @Test
    public void whenInvalidPaymentId_thenReturnUnprocessableEntity() {
        webTestClient.get()
                .uri(BASE_URL_PAYMENTS + "/" + INVALID_PAYMENT_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid paymentIdentifier provided: " + INVALID_PAYMENT_UUID);
    }

    @Test
    public void whenPaymentExistsOnDelete_thenReturnNoContent() {
        webTestClient.delete()
                .uri(BASE_URL_PAYMENTS + "/" + VALID_PAYMENT_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri(BASE_URL_PAYMENTS + "/" + VALID_PAYMENT_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Provided paymentIdentifier not found: " + VALID_PAYMENT_UUID);
    }

    @Test
    public void whenPostPaymentWithMissingFields_thenReturnBadRequest() {
        // Arrange
        PaymentRequestModel invalidRequest = PaymentRequestModel.builder()
                .paymentType(PaymentType.CREDIT_CARD)
                .transactionStatus(TransactionStatus.PENDING)
                .build(); // missing price and currency

        // Act & Assert
        webTestClient.post()
                .uri(BASE_URL_PAYMENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").exists();
    }

    @Test
    public void whenDeletingNonExistentPayment_thenReturnNotFound() {
        // Act & Assert
        webTestClient.delete()
                .uri(BASE_URL_PAYMENTS + "/" + NOTFOUND_PAYMENT_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message")
                .isEqualTo("Provided paymentIdentifier not found: " + NOTFOUND_PAYMENT_UUID);
    }

    @Test
    public void whenValidPaymentIsUpdated_thenReturnUpdatedPayment() {
        // Arrange
        Payment original = paymentRepository.findByPaymentIdentifier_PaymentId(VALID_PAYMENT_UUID);
        PaymentRequestModel updateRequest = PaymentRequestModel.builder()
                .price(original.getItemPrice().getPrice())
                .currency(original.getItemPrice().getCurrency())
                .paymentType(original.getPaymentType())
                .transactionStatus(TransactionStatus.COMPLETED)
                .build();

        // Act & Assert
        webTestClient.put()
                .uri(BASE_URL_PAYMENTS + "/" + VALID_PAYMENT_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.transactionStatus").isEqualTo("COMPLETED");
    }


}
