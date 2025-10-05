package pallares.gameassetmarketplace.apigateway.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;
import pallares.gameassetmarketplace.apigateway.utils.exceptions.*;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalControllerExceptionHandlerTest {
    private GlobalControllerExceptionHandler exceptionHandler;
    private WebRequest mockRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalControllerExceptionHandler();
        mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("uri=/test-path");
    }

    @Test
    void handleNotFoundException_returnsHttpErrorInfo() {
        NotFoundException ex = new NotFoundException("Not found error");
        HttpErrorInfo error = exceptionHandler.handleNotFoundException(mockRequest, ex);

        assertEquals(HttpStatus.NOT_FOUND, error.getHttpStatus());
        assertEquals("uri=/test-path", error.getPath());
        assertEquals("Not found error", error.getMessage());
        assertTimestampIsRecent(error.getTimestamp());
    }

    @Test
    void handleInvalidInputException_returnsHttpErrorInfo() {
        InvalidInputException ex = new InvalidInputException("Invalid input");
        HttpErrorInfo error = exceptionHandler.handleInvalidInputException(mockRequest, ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, error.getHttpStatus());
        assertEquals("uri=/test-path", error.getPath());
        assertEquals("Invalid input", error.getMessage());
        assertTimestampIsRecent(error.getTimestamp());
    }

    @Test
    void handleDuplicateVinException_returnsHttpErrorInfo() {
        DuplicateVinException ex = new DuplicateVinException("Duplicate VIN");
        HttpErrorInfo error = exceptionHandler.handleDuplicateVinException(mockRequest, ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, error.getHttpStatus());
        assertEquals("uri=/test-path", error.getPath());
        assertEquals("Duplicate VIN", error.getMessage());
        assertTimestampIsRecent(error.getTimestamp());
    }

    @Test
    void handleInvalidDiscountException_returnsHttpErrorInfo() {
        InvalidDiscountException ex = new InvalidDiscountException("Bad discount");
        HttpErrorInfo error = exceptionHandler.handleInvalidDiscountException(mockRequest, ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, error.getHttpStatus());
        assertEquals("uri=/test-path", error.getPath());
        assertEquals("Bad discount", error.getMessage());
        assertTimestampIsRecent(error.getTimestamp());
    }

    @Test
    void handleAssetAlreadyPurchasedException_returnsHttpErrorInfo() {
        AssetAlreadyPurchasedException ex = new AssetAlreadyPurchasedException("Already purchased");
        HttpErrorInfo error = exceptionHandler.handleAssetAlreadyPurchasedException(mockRequest, ex);

        assertEquals(HttpStatus.CONFLICT, error.getHttpStatus());
        assertEquals("uri=/test-path", error.getPath());
        assertEquals("Already purchased", error.getMessage());
        assertTimestampIsRecent(error.getTimestamp());
    }

    @Test
    void handleInvalidTransactionStateException_returnsHttpErrorInfo() {
        InvalidTransactionStateException ex = new InvalidTransactionStateException("Invalid state change");
        HttpErrorInfo error = exceptionHandler.handleInvalidTransactionStateException(mockRequest, ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, error.getHttpStatus());
        assertEquals("uri=/test-path", error.getPath());
        assertEquals("Invalid state change", error.getMessage());
        assertTimestampIsRecent(error.getTimestamp());
    }

    @Test
    void testHandleDuplicateEmailAddressException() {
        GlobalControllerExceptionHandler handler = new GlobalControllerExceptionHandler();
        WebRequest mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("/test/email");

        DuplicateEmailAddressException ex = new DuplicateEmailAddressException("Duplicate email");
        HttpErrorInfo response = handler.handleDuplicateEmailAddressException(mockRequest, ex);

        assertEquals(HttpStatus.CONFLICT, response.getHttpStatus());
        assertEquals("Duplicate email", response.getMessage());
        assertEquals("/test/email", response.getPath());
    }

    @Test
    void testHandleUsernameAlreadyTakenException() {
        GlobalControllerExceptionHandler handler = new GlobalControllerExceptionHandler();
        WebRequest mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("/test/username");

        UsernameAlreadyTakenException ex = new UsernameAlreadyTakenException("Username taken");
        HttpErrorInfo response = handler.handleUsernameAlreadyTakenException(mockRequest, ex);

        assertEquals(HttpStatus.CONFLICT, response.getHttpStatus());
        assertEquals("Username taken", response.getMessage());
        assertEquals("/test/username", response.getPath());
    }

    @Test
    void testHandleInvalidPhoneNumberException() {
        GlobalControllerExceptionHandler handler = new GlobalControllerExceptionHandler();
        WebRequest mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("/test/phone");

        InvalidPhoneNumberException ex = new InvalidPhoneNumberException("Invalid phone");
        HttpErrorInfo response = handler.handleInvalidPhoneNumberException(mockRequest, ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
        assertEquals("Invalid phone", response.getMessage());
        assertEquals("/test/phone", response.getPath());
    }

    @Test
    void testDuplicateVinExceptionConstructors() {
        assertNotNull(new DuplicateVinException());
        assertNotNull(new DuplicateVinException("Duplicate VIN"));
        assertNotNull(new DuplicateVinException(new RuntimeException("Cause")));
        assertNotNull(new DuplicateVinException("Message", new RuntimeException("Cause")));
    }

    @Test
    void testInvalidInputExceptionConstructors() {
        assertNotNull(new InvalidInputException());
        assertNotNull(new InvalidInputException("Invalid input"));
        assertNotNull(new InvalidInputException(new RuntimeException("Cause")));
        assertNotNull(new InvalidInputException("Message", new RuntimeException("Cause")));
    }

    @Test
    void testNotFoundExceptionConstructors() {
        assertNotNull(new NotFoundException());
        assertNotNull(new NotFoundException("Not found"));
        assertNotNull(new NotFoundException(new RuntimeException("Cause")));
        assertNotNull(new NotFoundException("Message", new RuntimeException("Cause")));
    }

    @Test
    void testDuplicateAssetNameExceptionConstructors() {
        assertNotNull(new DuplicateAssetNameException());
        assertNotNull(new DuplicateAssetNameException("Duplicate name"));
        assertNotNull(new DuplicateAssetNameException(new RuntimeException("Cause")));
        assertNotNull(new DuplicateAssetNameException("Message", new RuntimeException("Cause")));
    }

    @Test
    void testAssetFileMissingExceptionConstructors() {
        assertNotNull(new AssetFileMissingException());
        assertNotNull(new AssetFileMissingException("File missing"));
        assertNotNull(new AssetFileMissingException(new RuntimeException("Cause")));
        assertNotNull(new AssetFileMissingException("Message", new RuntimeException("Cause")));
    }

    @Test
    void testAssetAlreadyPurchasedExceptionConstructors() {
        assertNotNull(new AssetAlreadyPurchasedException());
        assertNotNull(new AssetAlreadyPurchasedException("Already purchased"));
        assertNotNull(new AssetAlreadyPurchasedException(new RuntimeException("Cause")));
        assertNotNull(new AssetAlreadyPurchasedException("Message", new RuntimeException("Cause")));
    }

    @Test
    void testInvalidDiscountExceptionConstructors() {
        assertNotNull(new InvalidDiscountException());
        assertNotNull(new InvalidDiscountException("Bad discount"));
        assertNotNull(new InvalidDiscountException(new RuntimeException("Cause")));
        assertNotNull(new InvalidDiscountException("Message", new RuntimeException("Cause")));
    }

    @Test
    void testInvalidTransactionStateExceptionConstructors() {
        assertNotNull(new InvalidTransactionStateException());
        assertNotNull(new InvalidTransactionStateException("Invalid state"));
        assertNotNull(new InvalidTransactionStateException(new RuntimeException("Cause")));
        assertNotNull(new InvalidTransactionStateException("Message", new RuntimeException("Cause")));
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

    @Test
    void testDuplicateEmailAddressExceptionConstructors() {
        DuplicateEmailAddressException ex1 = new DuplicateEmailAddressException();
        DuplicateEmailAddressException ex2 = new DuplicateEmailAddressException("Duplicate email");
        DuplicateEmailAddressException ex3 = new DuplicateEmailAddressException(new Throwable("Cause"));
        DuplicateEmailAddressException ex4 = new DuplicateEmailAddressException("Duplicate email", new Throwable("Cause"));
        assertAll(
                () -> assertNull(ex1.getMessage()),
                () -> assertEquals("Duplicate email", ex2.getMessage()),
                () -> assertEquals("Cause", ex3.getCause().getMessage()),
                () -> assertEquals("Duplicate email", ex4.getMessage()),
                () -> assertEquals("Cause", ex4.getCause().getMessage())
        );
    }

    @Test
    void testInvalidPhoneNumberExceptionConstructors() {
        InvalidPhoneNumberException ex1 = new InvalidPhoneNumberException();
        InvalidPhoneNumberException ex2 = new InvalidPhoneNumberException("Invalid phone");
        InvalidPhoneNumberException ex3 = new InvalidPhoneNumberException(new Throwable("Cause"));
        InvalidPhoneNumberException ex4 = new InvalidPhoneNumberException("Invalid phone", new Throwable("Cause"));
        assertAll(
                () -> assertNull(ex1.getMessage()),
                () -> assertEquals("Invalid phone", ex2.getMessage()),
                () -> assertEquals("Cause", ex3.getCause().getMessage()),
                () -> assertEquals("Invalid phone", ex4.getMessage())
        );
    }

    @Test
    void testUsernameAlreadyTakenExceptionConstructors() {
        UsernameAlreadyTakenException ex1 = new UsernameAlreadyTakenException();
        UsernameAlreadyTakenException ex2 = new UsernameAlreadyTakenException("Username taken");
        UsernameAlreadyTakenException ex3 = new UsernameAlreadyTakenException(new Throwable("Cause"));
        UsernameAlreadyTakenException ex4 = new UsernameAlreadyTakenException("Username taken", new Throwable("Cause"));
        assertAll(
                () -> assertNull(ex1.getMessage()),
                () -> assertEquals("Username taken", ex2.getMessage()),
                () -> assertEquals("Cause", ex3.getCause().getMessage()),
                () -> assertEquals("Username taken", ex4.getMessage())
        );
    }

    @Test
    void testInvalidEmailExceptionConstructors() {
        InvalidEmailException ex1 = new InvalidEmailException();
        InvalidEmailException ex2 = new InvalidEmailException("Invalid email format");
        InvalidEmailException ex3 = new InvalidEmailException(new Throwable("Cause"));
        InvalidEmailException ex4 = new InvalidEmailException("Invalid email format", new Throwable("Cause"));
        assertAll(
                () -> assertNull(ex1.getMessage()),
                () -> assertEquals("Invalid email format", ex2.getMessage()),
                () -> assertEquals("Cause", ex3.getCause().getMessage()),
                () -> assertEquals("Invalid email format", ex4.getMessage()),
                () -> assertEquals("Cause", ex4.getCause().getMessage())
        );
    }

    private void assertTimestampIsRecent(ZonedDateTime timestamp) {
        ZonedDateTime now = ZonedDateTime.now();
        assertNotNull(timestamp, "timestamp should not be null");
        assertFalse(timestamp.isAfter(now.plusSeconds(1)), "timestamp should not be in the future");
        assertFalse(timestamp.isBefore(now.minusSeconds(5)), "timestamp should be very recent");
    }
}