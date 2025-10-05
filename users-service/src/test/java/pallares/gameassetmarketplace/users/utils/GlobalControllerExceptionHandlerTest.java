package pallares.gameassetmarketplace.users.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;
import pallares.gameassetmarketplace.users.utils.exceptions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalControllerExceptionHandlerTest {

    private GlobalControllerExceptionHandler handler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalControllerExceptionHandler();
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("uri=/test");
    }

    @Test
    void testHandleDuplicateVinException() {
        GlobalControllerExceptionHandler handler = new GlobalControllerExceptionHandler();
        WebRequest mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("/test/path");

        DuplicateVinException ex = new DuplicateVinException("Duplicate VIN");
        HttpErrorInfo response = handler.handleDuplicateVinException(mockRequest, ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getHttpStatus());
        assertEquals("Duplicate VIN", response.getMessage());
        assertEquals("/test/path", response.getPath());
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
        InvalidInputException ex = new InvalidInputException("Invalid input");
        assertEquals("Invalid input", ex.getMessage());
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
}
