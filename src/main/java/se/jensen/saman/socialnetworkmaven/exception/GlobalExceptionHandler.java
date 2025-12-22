package se.jensen.saman.socialnetworkmaven.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException exception) {
        logger.warn("Handling bad request {}", exception.getBindingResult());
        Map<String, String> errors = new HashMap<>();
        for (org.springframework.validation.FieldError fieldError
                : exception.getBindingResult().getFieldErrors()) {
            String fieldName = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            errors.put(fieldName, message);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpectedErrors(Exception exception) {
        logger.error("Unexpected error has occurred {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server issues, something went wrong: " + exception.getMessage());

    }


    @ExceptionHandler(DuplicateUsernameExceptionHandler.class)
    public ResponseEntity<String> handleDuplicateUsername(DuplicateUsernameExceptionHandler exception) {
        logger.warn("Handling duplicate username error: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleAuthenticationError(BadCredentialsException exception) {
        logger.warn("Authentication failed: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccesDeniedException(AccessDeniedException exception) {
        logger.warn("Access denied: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(exception.getMessage());

    }

    @ExceptionHandler(OpenApiResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(OpenApiResourceNotFoundException exception) {
        logger.warn("Resource not found: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());

    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        logger.warn("Username not found: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(DuplicateEmailExceptionHandler.class)
    public ResponseEntity<String> handleDuplicateEmailException(DuplicateEmailExceptionHandler exception) {
        logger.warn("Email already in use: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleTokenExceptions(ExpiredJwtException exception) {
        logger.warn("Token invalid or expired {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(exception.getMessage());
    }

}