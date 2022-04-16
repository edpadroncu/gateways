package com.edpadron.gateways.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ApiError {

    private String message;
    private List<String> errors;
    private String debug_error;

    public ApiError(String message, String error) {
        this.message = message;
        errors = Arrays.asList(error);
        this.debug_error = "";
    }

    public ApiError(String message, String error, String debug_error) {
        this.message = message;
        this.errors = Arrays.asList(error);
        this.setDebug_error(debug_error);
    }

    public ApiError(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
        this.debug_error = "";
    }
}
