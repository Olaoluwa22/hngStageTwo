package com.hng.stagetwo.exception.errorHandler;

public class ErrorField {
    private String field;
    private String message;

    public ErrorField(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
