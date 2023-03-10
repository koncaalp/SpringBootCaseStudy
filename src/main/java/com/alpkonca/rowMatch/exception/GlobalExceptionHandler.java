package com.alpkonca.rowMatch.exception;

import com.alpkonca.rowMatch.payload.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// GlobalExceptionHandler for handling all exceptions, includes custom and default exceptions. Returns ErrorResponse object with error details and date of error.
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler { // extends ResponseEntityExceptionHandler for custom validation response


    @ExceptionHandler(InsufficientBalanceException.class) // handles InsufficientBalanceException when the user has insufficient balance to perform an action
    public ResponseEntity<ErrorResponse> handleInsufficientBalanceException(InsufficientBalanceException ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), webRequest.getDescription(false), "insufficient_balance", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(ResourceWithIdNotFoundException.class) // handles ResourceNotFoundException when the resource is not found
    public ResponseEntity<ErrorResponse> handleResourceWithIdNotFound(ResourceWithIdNotFoundException ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), webRequest.getDescription(false), "resource_withId_not_found",ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(NoResourcesFoundException.class) // handles NoResourcesFoundException when there is no resource found in the database table
    public ResponseEntity<ErrorResponse> handleNoResourcesFound(NoResourcesFoundException ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(), webRequest.getDescription(false), "no_resources_found",ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UniqueFieldException.class) // handles UniqueFieldException when there is another record with the same unique field in the database table
    public ResponseEntity<ErrorResponse> handleUniqueFieldException(UniqueFieldException ex, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(new Date(),webRequest.getDescription(false), "unique_field",ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class) // handle all other exceptions
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest webRequest) {
        ErrorResponse error = new ErrorResponse(new Date(), webRequest.getDescription(false), "error",ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override // override handleMethodArgumentNotValid method for custom validation response
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> { // get all errors and add them to map with field name and error message
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // return map of errors and set status code to BAD_REQUEST
    }
}

