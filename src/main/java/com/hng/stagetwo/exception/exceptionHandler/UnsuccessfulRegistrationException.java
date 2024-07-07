package com.hng.stagetwo.exception.exceptionHandler;

public class UnsuccessfulRegistrationException extends RuntimeException{
    public UnsuccessfulRegistrationException(String message){
        super(message);
    }
}
