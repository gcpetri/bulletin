package com.tamudatathon.bulletin.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Login redirect failed") 
public class LoginRedirectException extends RuntimeException {
    public LoginRedirectException(String message) {
        super("Login redirect failed: " + message);
    }
}
