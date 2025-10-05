package pallares.gameassetmarketplace.payments.utils;

import org.junit.jupiter.api.Test;
import pallares.gameassetmarketplace.payments.utils.exceptions.*;

import static org.junit.jupiter.api.Assertions.*;

class GlobalControllerExceptionHandlerTest {

    @Test
    void testDuplicateVinExceptionConstructors() {
        assertNotNull(new DuplicateVinException());
        assertNotNull(new DuplicateVinException("Duplicate VIN"));
        assertNotNull(new DuplicateVinException(new RuntimeException("Cause")));
        assertNotNull(new DuplicateVinException("Duplicate VIN", new RuntimeException("Cause")));
    }

    @Test
    void testInvalidInputExceptionConstructors() {
        assertNotNull(new InvalidInputException());
        assertNotNull(new InvalidInputException("Invalid input"));
        assertNotNull(new InvalidInputException(new RuntimeException("Cause")));
        assertNotNull(new InvalidInputException("Invalid input", new RuntimeException("Cause")));
    }

    @Test
    void testNotFoundExceptionConstructors() {
        assertNotNull(new NotFoundException());
        assertNotNull(new NotFoundException("Not found"));
        assertNotNull(new NotFoundException(new RuntimeException("Cause")));
        assertNotNull(new NotFoundException("Not found", new RuntimeException("Cause")));
    }

    @Test
    void testInvalidTransactionAmountExceptionConstructors() {
        assertNotNull(new InvalidTransactionAmountException());
        assertNotNull(new InvalidTransactionAmountException("Invalid amount"));
        assertNotNull(new InvalidTransactionAmountException(new RuntimeException("Cause")));
        assertNotNull(new InvalidTransactionAmountException("Invalid amount", new RuntimeException("Cause")));
    }

    @Test
    void testUnsupportedPaymentTypeExceptionConstructors() {
        assertNotNull(new UnsupportedPaymentTypeException());
        assertNotNull(new UnsupportedPaymentTypeException("Unsupported type"));
        assertNotNull(new UnsupportedPaymentTypeException(new RuntimeException("Cause")));
        assertNotNull(new UnsupportedPaymentTypeException("Unsupported type", new RuntimeException("Cause")));
    }
}
