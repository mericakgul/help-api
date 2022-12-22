package com.mericakgul.helpapi.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // This exception handler is to validate if the dto in the request is valid. To do that we add @Valid annotation next to @RequestBody annotation in controller method.
    // So if a field in dto object has @NotNull annotation and if this field is sent null in the request by client then we handle this exception thanks to this handler,
    // so the validation is done in the controller layer before reaching the service layer.
    // @Valid annotation is doing the validation by checking the annotations added to the fields of the dto models but only throws exception. Here we just handle that exception.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ExceptionMessage> handleExceptions(BadCredentialsException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ExceptionMessage.builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .message("Username or password is invalid.")
                        .timestamp(new Date()).build());
    }
}
