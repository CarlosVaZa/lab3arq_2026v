package com.udea.banco2026v.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test-path");
    }

    @Test
    void handleNotFound_ShouldReturn404() {
        NotFoundException ex = new NotFoundException("Not found");
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("NOT_FOUND", response.getBody().get("code"));
        assertEquals("Not found", response.getBody().get("message"));
        assertEquals("/test-path", response.getBody().get("path"));
    }

    @Test
    void handleBadRequest_ShouldReturn400() {
        IllegalArgumentException ex = new IllegalArgumentException("Bad request");
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleBadRequest(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("BUSINESS_RULE_VIOLATION", response.getBody().get("code"));
    }

    @Test
    void handleBusinessRule_ShouldReturn422() {
        IllegalStateException ex = new IllegalStateException("Business rule violation");
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleBusinessRule(ex, request);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("BUSINESS_RULE_VIOLATION", response.getBody().get("code"));
    }

    @Test
    void handleDataConflict_ShouldReturn409() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Conflict");
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleDataConflict(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("DATA_INTEGRITY_VIOLATION", response.getBody().get("code"));
    }

    @Test
    void handleUnexpected_ShouldReturn500() {
        Exception ex = new Exception("Unexpected");
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleUnexpected(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("INTERNAL_ERROR", response.getBody().get("code"));
    }

    @Test
    void handleConstraintViolation_ShouldReturn400() {
        ConstraintViolationException ex = new ConstraintViolationException("Violation", new HashSet<>());
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleConstraintViolation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("VALIDATION_ERROR", response.getBody().get("code"));
    }

    @Test
    void handleMethodArgumentNotValid_ShouldReturn400() {
        MethodArgumentNotValidException ex = Mockito.mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "must not be null");
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleMethodArgumentNotValid(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("VALIDATION_ERROR", response.getBody().get("code"));
        assertTrue(response.getBody().get("message").toString().contains("field: must not be null"));
    }
}
