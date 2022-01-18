package com.tamudatathon.bulletin.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid Event request body") 
public class EventInvalidException extends RuntimeException {
    public EventInvalidException() {
        super("Invalid request body for Event");
    }
}
