package com.mt.jwtstarter.config;

import com.mt.jwtstarter.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFound ex) {
        Map<String, String> error = Collections.singletonMap("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        Map<String, String> error = Collections.singletonMap("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

}