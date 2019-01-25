package com.xebialabs.craftsmanship.helper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TooMuchShotException.class)
    public final ResponseEntity<ErrorDetails> handleTooMuchSalvoException(TooMuchShotException ex,  WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundUserException.class)
    public final ResponseEntity<ErrorDetails> handleNotFoundUserException(NotFoundUserException ex, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleEntityNotFoundException(EntityNotFoundException ex,  WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UniqueConstraintException.class)
    public final ResponseEntity<ErrorDetails> handleUniqueConstraintException(UniqueConstraintException ex,  WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotYourTurnException.class)
    public final ResponseEntity<ErrorDetails> handleNotYourTurnException(NotYourTurnException ex,  WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }
}