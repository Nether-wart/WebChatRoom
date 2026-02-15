package io.github.nether_wart.controller;

import io.github.nether_wart.entity.ApiResponse;
import io.github.nether_wart.exception.NotFoundException;
import io.github.nether_wart.exception.UnauthorizedException;
import io.github.nether_wart.exception.VerifiedUserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(exception = UsernameNotFoundException.class)
    public ApiResponse<?> handleNotFound(UsernameNotFoundException ex) {
        return new ApiResponse<>(-1, "user " + ex.getName() + " not found", null);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized() {
        logger.warn("unauthorized request entered authentication required controller");
        return ResponseEntity.status(401).build();
    }

    @ExceptionHandler(VerifiedUserNotFoundException.class)
    public ResponseEntity<?> handleError() {
        logger.warn("user token is valid,but user not found");
        return ResponseEntity.internalServerError().body(new ApiResponse<>(-500, "internal server error", null));
    }

    @ExceptionHandler(NotFoundException.class)
    public ApiResponse<?> handleNotFound() {
        return ApiResponse.notFound();
    }
}
