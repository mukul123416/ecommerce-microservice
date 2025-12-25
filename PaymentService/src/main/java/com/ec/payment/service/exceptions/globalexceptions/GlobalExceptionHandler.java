package com.ec.payment.service.exceptions.globalexceptions;
import com.ec.payment.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.payment.service.payloads.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private ErrorResponse errorResponse;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound (ResourceNotFoundException ex) {
        return errorResponse.responseHandler(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // For @Valid errors (body)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        final String[] message = new String[1];
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String defaultMessage = error.getDefaultMessage();
            message[0] = defaultMessage;
        });
        return errorResponse.responseHandler(message[0], HttpStatus.BAD_REQUEST);
    }

    // For @Validated errors (query params, path variables)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolations(ConstraintViolationException ex) {
        final String[] message = new String[1];
        ex.getConstraintViolations().forEach(cv -> {
            String defaultMessage = cv.getMessage();
            message[0] = defaultMessage;
        });
        return errorResponse.responseHandler(message[0], HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String error = "Invalid value for parameter '" + ex.getName() + "'. Expected type: " + ex.getRequiredType().getSimpleName();
        return errorResponse.responseHandler(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleRequestParameter(MissingServletRequestParameterException ex) {
        String error = "Invalid value for parameter '" + ex.getParameterName() + "'. Expected type: " + ex.getParameterType();
        return errorResponse.responseHandler(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = ex.getMessage().split(":")[0];
        return errorResponse.responseHandler(message, HttpStatus.BAD_REQUEST);
    }

}
