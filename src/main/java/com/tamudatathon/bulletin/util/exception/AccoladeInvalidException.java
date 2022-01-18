package com.tamudatathon.bulletin.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid Accolade request body") 
public class AccoladeInvalidException extends RuntimeException {
    public AccoladeInvalidException(String message) {
        super("Invalid request body for accolade: " + message);
    }
}