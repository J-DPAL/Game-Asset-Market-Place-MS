package pallares.gameassetmarketplace.apigateway.utils;

import pallares.gameassetmarketplace.apigateway.utils.exceptions.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public HttpErrorInfo handleNotFoundException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public HttpErrorInfo handleInvalidInputException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(DuplicateVinException.class)
    public HttpErrorInfo handleDuplicateVinException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(DuplicateEmailAddressException.class)
    public HttpErrorInfo handleDuplicateEmailAddressException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(CONFLICT, request, ex);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public HttpErrorInfo handleUsernameAlreadyTakenException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(CONFLICT, request, ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(InvalidPhoneNumberException.class)
    public HttpErrorInfo handleInvalidPhoneNumberException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(BAD_REQUEST, request, ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(InvalidEmailException.class)
    public HttpErrorInfo handleInvalidEmailException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(BAD_REQUEST, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidTransactionAmountException.class)
    public HttpErrorInfo handleInvalidTransactionAmountException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(UnsupportedPaymentTypeException.class)
    public HttpErrorInfo handleUnsupportedPaymentTypeException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(BAD_REQUEST, request, ex);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidDiscountException.class)
    public HttpErrorInfo handleInvalidDiscountException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AssetAlreadyPurchasedException.class)
    public HttpErrorInfo handleAssetAlreadyPurchasedException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(HttpStatus.CONFLICT, request, ex);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidTransactionStateException.class)
    public HttpErrorInfo handleInvalidTransactionStateException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(DuplicateAssetNameException.class)
    public HttpErrorInfo handleDuplicateAssetNameException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(CONFLICT, request, ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(AssetFileMissingException.class)
    public HttpErrorInfo handleAssetFileMissingException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(BAD_REQUEST, request, ex);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, WebRequest request, Exception ex) {
        final String path = request.getDescription(false);
        // final String path = request.getPath().pathWithinApplication().value();
        final String message = ex.getMessage();
        log.debug("message is: " + message);

        log.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);

        return new HttpErrorInfo(httpStatus, path, message);
    }
}
