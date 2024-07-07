package com.hng.stagetwo.response;

import com.hng.stagetwo.exception.errorHandler.ErrorField;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    private List<ErrorField> errors = new ArrayList<>();

    public List<ErrorField> getErrors() {
        return errors;
    }
    public void setErrors(List<ErrorField> errors) {
        this.errors = errors;
    }
    public void addError(String field, String message) {
        this.errors.add(new ErrorField(field, message));
    }
}
