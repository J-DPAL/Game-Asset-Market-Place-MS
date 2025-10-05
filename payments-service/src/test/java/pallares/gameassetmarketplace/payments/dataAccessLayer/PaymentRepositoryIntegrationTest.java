package pallares.gameassetmarketplace.payments.dataAccessLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PaymentRepositoryIntegrationTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
    }

    @Test
    void whenPaymentsExist_thenReturnAllPayments() {
        // arrange
        Payment payment1 = new Payment(new ItemPrice(new BigDecimal("19.99"), Currency.CAD),
                PaymentType.CREDIT_CARD, TransactionStatus.COMPLETED);

        Payment payment2 = new Payment(new ItemPrice(new BigDecimal("49.99"), Currency.CAD),
                PaymentType.PAYPAL, TransactionStatus.PENDING);

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        long afterSizeDB = paymentRepository.count();

        // act
        List<Payment> payments = paymentRepository.findAll();

        // assert
        assertNotNull(payments);
        assertThat(payments, hasSize(2));
        assertNotEquals(0, afterSizeDB);
        assertEquals(afterSizeDB, payments.size());
    }

    @Test
    void whenPaymentIsSaved_thenItCanBeRetrievedByPublicId() {
        // arrange
        Payment payment = new Payment(new ItemPrice(new BigDecimal("15.00"), Currency.US),
                PaymentType.CREDIT_CARD, TransactionStatus.FAILED);

        Payment savedPayment = paymentRepository.save(payment);
        String paymentId = savedPayment.getPaymentIdentifier().getPaymentId();

        // act
        Payment found = paymentRepository.findByPaymentIdentifier_PaymentId(paymentId);

        // assert
        assertNotNull(found);
        assertEquals(paymentId, found.getPaymentIdentifier().getPaymentId());
        assertEquals(payment.getItemPrice().getPrice(), found.getItemPrice().getPrice());
        assertEquals(payment.getPaymentType(), found.getPaymentType());
        assertEquals(payment.getTransactionStatus(), found.getTransactionStatus());
    }

    @Test
    void whenPaymentIsNotFound_thenReturnNull() {
        // arrange
        final String NON_EXISTENT_ID = "d1a5cb67-e289-4c9d-99df-9a1e65bb714a";

        // act
        Payment found = paymentRepository.findByPaymentIdentifier_PaymentId(NON_EXISTENT_ID);

        // assert
        assertNull(found);
    }

    @Test
    void whenValidPaymentIsSaved_thenItPersistsCorrectly() {
        // arrange
        ItemPrice price = new ItemPrice(new BigDecimal("79.99"), Currency.EUR);
        Payment payment = new Payment(price, PaymentType.DEBIT_CARD, TransactionStatus.COMPLETED);

        // act
        Payment saved = paymentRepository.save(payment);

        // assert
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertNotNull(saved.getPaymentIdentifier());
        assertNotNull(saved.getPaymentIdentifier().getPaymentId());
        assertEquals(price.getPrice(), saved.getItemPrice().getPrice());
        assertEquals(price.getCurrency(), saved.getItemPrice().getCurrency());
        assertEquals(payment.getPaymentType(), saved.getPaymentType());
        assertEquals(payment.getTransactionStatus(), saved.getTransactionStatus());
    }

    @Test
    void whenPaymentIsUpdated_thenChangesPersist() {
        // arrange
        Payment payment = new Payment(new ItemPrice(new BigDecimal("5.00"), Currency.US),
                PaymentType.CREDIT_CARD, TransactionStatus.PENDING);
        Payment saved = paymentRepository.save(payment);

        // act
        saved.setTransactionStatus(TransactionStatus.COMPLETED);
        Payment updated = paymentRepository.save(saved);

        // assert
        Payment found = paymentRepository.findByPaymentIdentifier_PaymentId(updated.getPaymentIdentifier().getPaymentId());
        assertNotNull(found);
        assertEquals(TransactionStatus.COMPLETED, found.getTransactionStatus());
    }

    @Test
    void whenPaymentIsDeleted_thenItIsRemovedFromRepository() {
        // arrange
        Payment payment = new Payment(new ItemPrice(new BigDecimal("3.99"), Currency.CAD),
                PaymentType.PAYPAL, TransactionStatus.COMPLETED);
        Payment saved = paymentRepository.save(payment);
        String id = saved.getPaymentIdentifier().getPaymentId();

        // act
        paymentRepository.delete(saved);

        // assert
        assertNull(paymentRepository.findByPaymentIdentifier_PaymentId(id));
        assertEquals(0, paymentRepository.count());
    }

    @Test
    void whenPaymentAmountIsZero_thenPaymentIsStillSavedInRepo() {
        // Arrange
        ItemPrice zeroPrice = new ItemPrice(BigDecimal.ZERO, Currency.CAD);
        Payment payment = new Payment(zeroPrice, PaymentType.PAYPAL, TransactionStatus.PENDING);

        // Act
        Payment saved = paymentRepository.save(payment);

        // Assert
        assertNotNull(saved);
        assertEquals(BigDecimal.ZERO, saved.getItemPrice().getPrice());
    }


}
