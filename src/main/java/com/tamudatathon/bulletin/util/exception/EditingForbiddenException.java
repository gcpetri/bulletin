package com.tamudatathon.bulletin.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason="Editting Forbidden") 
public class EditingForbiddenException extends RuntimeException {
    public EditingForbiddenException(String message) {
        super("Editing Forbidden: " + message);
    }
}