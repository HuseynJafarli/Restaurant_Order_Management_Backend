package com.romb.rombApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequest(BadRequestException ex, WebRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleJwt(JwtException ex, WebRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "JWT ERROR");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex, WebRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(InvalidCredentialsException ex, WebRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid credentials: " + ex.getMessage());
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<Object> handleNoContent(NoContentException ex, WebRequest request) {
        return buildResponse(HttpStatus.NO_CONTENT, ex.getMessage());
    }
    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("status", status.value());
        errorBody.put("message", message);

        return new ResponseEntity<>(errorBody, status);
    }
}
