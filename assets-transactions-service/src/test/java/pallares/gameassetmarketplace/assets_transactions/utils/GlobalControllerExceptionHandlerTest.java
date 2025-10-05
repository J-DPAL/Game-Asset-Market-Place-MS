package pallares.gameassetmarketplace.assets_transactions.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;
import pallares.gameassetmarketplace.assets_transactions.utils.exceptions.*;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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


    private void assertTimestampIsRecent(ZonedDateTime timestamp) {
        ZonedDateTime now = ZonedDateTime.now();
        assertNotNull(timestamp, "timestamp should not be null");
        assertFalse(timestamp.isAfter(now.plusSeconds(1)), "timestamp should not be in the future");
        assertFalse(timestamp.isBefore(now.minusSeconds(5)), "timestamp should be very recent");
    }
}
