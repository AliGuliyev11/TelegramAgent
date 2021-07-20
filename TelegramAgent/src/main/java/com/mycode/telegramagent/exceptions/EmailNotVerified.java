package com.mycode.telegramagent.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailNotVerified extends RuntimeException{
    public EmailNotVerified() {
        super("Email not verified");
    }
}
