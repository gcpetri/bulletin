package com.tamudatathon.bulletin.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="No such Accolade") 
public class AccoladeNotFoundException extends RuntimeException {
    public AccoladeNotFoundException(Long id) {
        super("Could not find accolade with id " + id);
    }
}