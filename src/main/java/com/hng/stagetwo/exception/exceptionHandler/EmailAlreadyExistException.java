package com.hng.stagetwo.exception.exceptionHandler;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String message){
        super(message);
    }
}
