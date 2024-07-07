package com.hng.stagetwo.exception.exceptionHandler;

import com.hng.stagetwo.response.ErrorResponse;
import com.hng.stagetwo.response.ExceptionResponse;
import com.hng.stagetwo.utils.ConstantMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<?> emailAlreadyExistException(EmailAlreadyExistException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.addError("email", exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidaException(MethodArgumentNotValidException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errorResponse.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<?> invalidCredentialException(InvalidCredentialException exception) {
        ExceptionResponse<?> exceptionResponse = new ExceptionResponse<>();
        exceptionResponse.setStatus(ConstantMessages.BAD_REQUEST.getMessage());
        exceptionResponse.setMessage(ConstantMessages.FAILED_AUTHENTICATION.getMessage());
        exceptionResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(UnsuccessfulRegistrationException.class)
    public ResponseEntity<?> unsuccessfulRegistrationException(UnsuccessfulRegistrationException exception) {
        ExceptionResponse<?> exceptionResponse = new ExceptionResponse<>();
        exceptionResponse.setStatus(ConstantMessages.BAD_REQUEST.getMessage());
        exceptionResponse.setMessage(ConstantMessages.REGISTRATION_UNSUCCESSFUL.getMessage());
        exceptionResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException exception) {
        ExceptionResponse<?> exceptionResponse = new ExceptionResponse<>();
        exceptionResponse.setStatus(ConstantMessages.NOT_FOUND.getMessage());
        exceptionResponse.setMessage(ConstantMessages.RESOURCE_NOT_FOUND.getMessage());
        exceptionResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UnsuccessfulCreationException.class)
    public ResponseEntity<?> unsuccessfulCreationException(UnsuccessfulCreationException exception) {
        ExceptionResponse<?> exceptionResponse = new ExceptionResponse<>();
        exceptionResponse.setStatus(ConstantMessages.BAD_REQUEST.getMessage());
        exceptionResponse.setMessage(ConstantMessages.CLIENT_ERROR.getMessage());
        exceptionResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}