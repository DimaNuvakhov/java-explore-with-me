package ru.practicum.controller;


import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.*;
import ru.practicum.exception.IllegalStateException;
import ru.practicum.model.ApiError;

import java.util.Arrays;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<Object> handleThrowable(Throwable ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        String reason = "Internal server error.";
        if (Arrays.stream(ex.getStackTrace()).findFirst().isPresent()) {
            apiError.setErrors(Arrays.stream(ex.getStackTrace()).findFirst().get().toString());
        } else {
            apiError.setErrors(null);
        }
        apiError.setReason(reason);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        String reason = "For the requested operation the conditions are not met.";
        if (Arrays.stream(ex.getStackTrace()).findFirst().isPresent()) {
            apiError.setErrors(Arrays.stream(ex.getStackTrace()).findFirst().get().toString());
        } else {
            apiError.setErrors(null);
        }
        apiError.setReason(reason);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        String reason = "For the requested operation the conditions are not met.";
        if (Arrays.stream(ex.getStackTrace()).findFirst().isPresent()) {
            apiError.setErrors(Arrays.stream(ex.getStackTrace()).findFirst().get().toString());
        } else {
            apiError.setErrors(null);
        }
        apiError.setReason(reason);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        String reason = "The required object was not found.";
        if (Arrays.stream(ex.getStackTrace()).findFirst().isPresent()) {
            apiError.setErrors(Arrays.stream(ex.getStackTrace()).findFirst().get().toString());
        } else {
            apiError.setErrors(null);
        }
        apiError.setReason(reason);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(EventNotFoundException.class)
    protected ResponseEntity<Object> handleEventNotFoundException(EventNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        String reason = "The required object was not found.";
        if (Arrays.stream(ex.getStackTrace()).findFirst().isPresent()) {
            apiError.setErrors(Arrays.stream(ex.getStackTrace()).findFirst().get().toString());
        } else {
            apiError.setErrors(null);
        }
        apiError.setReason(reason);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    protected ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        String reason = "The required object was not found.";
        if (Arrays.stream(ex.getStackTrace()).findFirst().isPresent()) {
            apiError.setErrors(Arrays.stream(ex.getStackTrace()).findFirst().get().toString());
        } else {
            apiError.setErrors(null);
        }
        apiError.setReason(reason);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ParticipationRequestNotFoundException.class)
    protected ResponseEntity<Object> handleParticipationRequestNotFoundException(ParticipationRequestNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        String reason = "The required object was not found.";
        if (Arrays.stream(ex.getStackTrace()).findFirst().isPresent()) {
            apiError.setErrors(Arrays.stream(ex.getStackTrace()).findFirst().get().toString());
        } else {
            apiError.setErrors(null);
        }
        apiError.setReason(reason);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(IllegalDateException.class)
    protected ResponseEntity<Object> handleIllegalDateException(IllegalDateException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        String reason = "For the requested operation the conditions are not met.";
        if (Arrays.stream(ex.getStackTrace()).findFirst().isPresent()) {
            apiError.setErrors(Arrays.stream(ex.getStackTrace()).findFirst().get().toString());
        } else {
            apiError.setErrors(null);
        }
        apiError.setReason(reason);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(IllegalIdException.class)
    protected ResponseEntity<Object> handleIllegalIdException(IllegalIdException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        String reason = "For the requested operation the conditions are not met.";
        if (Arrays.stream(ex.getStackTrace()).findFirst().isPresent()) {
            apiError.setErrors(Arrays.stream(ex.getStackTrace()).findFirst().get().toString());
        } else {
            apiError.setErrors(null);
        }
        apiError.setReason(reason);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex) {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN);
        apiError.setMessage(ex.getMessage());
        String reason = "For the requested operation the conditions are not met.";
        if (Arrays.stream(ex.getStackTrace()).findFirst().isPresent()) {
            apiError.setErrors(Arrays.stream(ex.getStackTrace()).findFirst().get().toString());
        } else {
            apiError.setErrors(null);
        }
        apiError.setReason(reason);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidAccessException.class)
    protected ResponseEntity<Object> handleInvalidAccessException(InvalidAccessException ex) {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN);
        apiError.setMessage(ex.getMessage());
        String reason = "For the requested operation the conditions are not met.";
        if (Arrays.stream(ex.getStackTrace()).findFirst().isPresent()) {
            apiError.setErrors(Arrays.stream(ex.getStackTrace()).findFirst().get().toString());
        } else {
            apiError.setErrors(null);
        }
        apiError.setReason(reason);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ParticipantLimitException.class)
    protected ResponseEntity<Object> handleParticipantLimitException(ParticipantLimitException ex) {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN);
        apiError.setMessage(ex.getMessage());
        String reason = "For the requested operation the conditions are not met.";
        if (Arrays.stream(ex.getStackTrace()).findFirst().isPresent()) {
            apiError.setErrors(Arrays.stream(ex.getStackTrace()).findFirst().get().toString());
        } else {
            apiError.setErrors(null);
        }
        apiError.setReason(reason);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(CompilationNotFoundException.class)
    protected ResponseEntity<Object> handleCompilationNotFoundException(CompilationNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        String reason = "The required object was not found.";
        if (Arrays.stream(ex.getStackTrace()).findFirst().isPresent()) {
            apiError.setErrors(Arrays.stream(ex.getStackTrace()).findFirst().get().toString());
        } else {
            apiError.setErrors(null);
        }
        apiError.setReason(reason);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(CompilationEventsNotFoundException.class)
    protected ResponseEntity<Object> handleCompilationEventsNotFoundException(CompilationEventsNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        String reason = "The required object was not found.";
        if (Arrays.stream(ex.getStackTrace()).findFirst().isPresent()) {
            apiError.setErrors(Arrays.stream(ex.getStackTrace()).findFirst().get().toString());
        } else {
            apiError.setErrors(null);
        }
        apiError.setReason(reason);
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
