package com.flzs.logistics_core.util.handler;

import com.flzs.logistics_core.exception.*;
import com.flzs.logistics_core.util.constants.UiMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ********* External API Errors *********

    @ExceptionHandler(ExternalApiUnavailableException.class)
    public ResponseEntity<Map<String, Object>> handleExternalApiUnavailable(ExternalApiUnavailableException ex) {
        return build(HttpStatus.SERVICE_UNAVAILABLE, UiMessages.ERROR_SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleLocationNotFound(LocationNotFoundException ex) {
        if (ex.getMunicipality() != null && ex.getProvince() != null) {
            String message = String.format(UiMessages.ERROR_MUNICIPALITY_NOT_FOUND, ex.getMunicipality(), ex.getProvince());
            return build(HttpStatus.NOT_FOUND, message);
        }
        return build(HttpStatus.NOT_FOUND, UiMessages.ERROR_LOCATION_NOT_FOUND);
    }

    @ExceptionHandler(RouteCalculationException.class)
    public ResponseEntity<Map<String, Object>> handleRouteCalculation(RouteCalculationException ex) {
        return build(HttpStatus.BAD_REQUEST, UiMessages.ERROR_ROUTE_CALCULATION);
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<Map<String, Object>> handleExternalApiGeneral(ExternalApiException ex) {
        return build(HttpStatus.BAD_GATEWAY, UiMessages.ERROR_EXTERNAL_SERVICE);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(NoResourceFoundException ex) {
        logger.warn("Resource not found: {}", ex.getResourcePath());
        return build(HttpStatus.NOT_FOUND, UiMessages.ERROR_SERVICE_UNAVAILABLE);
    }

    // ********* Fallback for Unexpected Errors *********

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        logger.error("Unexpected error", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, UiMessages.ERROR_EXTERNAL_SERVICE);
    }

    // ********* Helper *********

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        body.put("timestamp", Instant.now());
        return ResponseEntity.status(status).body(body);
    }
}