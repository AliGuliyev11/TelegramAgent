package com.mycode.telegramagent.exceptions.handlers;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

@Data
public class ErrorMessage {
    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ErrorMessage(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ErrorMessage(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
}
