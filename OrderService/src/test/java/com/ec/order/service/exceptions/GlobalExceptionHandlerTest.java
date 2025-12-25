package com.ec.order.service.exceptions;

import com.ec.order.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.order.service.exceptions.globalexceptions.GlobalExceptionHandler;
import com.ec.order.service.payloads.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    @Mock
    private ErrorResponse errorResponse;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Order not found");

        when(errorResponse.responseHandler("Order not found", HttpStatus.NOT_FOUND))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error"));

        ResponseEntity<?> response = globalExceptionHandler.handleResourceNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(errorResponse, times(1)).responseHandler("Order not found", HttpStatus.NOT_FOUND);
    }

    @Test
    void testHandleConstraintViolation() {
        ConstraintViolation<String> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Invalid parameter");

        ConstraintViolationException ex = new ConstraintViolationException(new HashSet<>(Collections.singletonList(violation)));

        when(errorResponse.responseHandler("Invalid parameter", HttpStatus.BAD_REQUEST))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error"));

        ResponseEntity<?> response = globalExceptionHandler.handleConstraintViolations(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(errorResponse, times(1)).responseHandler("Invalid parameter", HttpStatus.BAD_REQUEST);
    }

    @Test
    void testHandleTypeMismatch() {
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "abc", Integer.class, "id", null, null
        );

        String expectedMsg = "Invalid value for parameter 'id'. Expected type: Integer";
        when(errorResponse.responseHandler(expectedMsg, HttpStatus.BAD_REQUEST))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error"));

        ResponseEntity<?> response = globalExceptionHandler.handleTypeMismatch(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(errorResponse, times(1)).responseHandler(expectedMsg, HttpStatus.BAD_REQUEST);
    }

    @Test
    void testHandleMissingServletRequestParameter() throws Exception {
        MissingServletRequestParameterException ex =
                new MissingServletRequestParameterException("name", "String");

        String expectedMsg = "Invalid value for parameter 'name'. Expected type: String";
        when(errorResponse.responseHandler(expectedMsg, HttpStatus.BAD_REQUEST))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error"));

        ResponseEntity<?> response = globalExceptionHandler.handleRequestParameter(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(errorResponse, times(1)).responseHandler(expectedMsg, HttpStatus.BAD_REQUEST);
    }

    @Test
    void testHandleHttpMessageNotReadable() {
        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("Invalid JSON", (HttpInputMessage) null);

        String expected = "Invalid JSON";

        when(errorResponse.responseHandler(expected, HttpStatus.BAD_REQUEST))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error"));

        ResponseEntity<?> response = globalExceptionHandler.handleMessageNotReadable(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(errorResponse, times(1))
                .responseHandler(expected, HttpStatus.BAD_REQUEST);
    }
}
