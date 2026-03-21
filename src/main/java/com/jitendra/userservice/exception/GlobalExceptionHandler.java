package com.jitendra.userservice.exception;

import com.jitendra.userservice.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserExists(
            UserAlreadyExistsException ex) {

        ErrorResponse error =
                new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }




        // 404
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiError> handleNotFound(
                ResourceNotFoundException ex,
                HttpServletRequest request) {

            ApiError error = new ApiError(
                    HttpStatus.NOT_FOUND.value(),
                    ex.getMessage(),
                    request.getRequestURI(),
                    LocalDateTime.now()
            );

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        // 409
        @ExceptionHandler(DuplicateResourceException.class)
        public ResponseEntity<ApiError> handleDuplicate(
                DuplicateResourceException ex,
                HttpServletRequest request) {

            ApiError error = new ApiError(
                    HttpStatus.CONFLICT.value(),
                    ex.getMessage(),
                    request.getRequestURI(),
                    LocalDateTime.now()
            );

            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }

        // 400
        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ApiError> handleBadRequest(
                BadRequestException ex,
                HttpServletRequest request) {

            ApiError error = new ApiError(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.getMessage(),
                    request.getRequestURI(),
                    LocalDateTime.now()
            );

            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        // Validation Errors 🔥
        @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleValidation(
                Exception ex,
                HttpServletRequest request) {

            ApiError error = new ApiError(
                    HttpStatus.BAD_REQUEST.value(),
                    "Validation failed",
                    request.getRequestURI(),
                    LocalDateTime.now()
            );

            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        // Generic Exception 🔥
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleGeneric(
                Exception ex,
                HttpServletRequest request) {

            ApiError error = new ApiError(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ex.getMessage(),
                    request.getRequestURI(),
                    LocalDateTime.now()
            );

            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }