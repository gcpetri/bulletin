package com.tamudatathon.bulletin.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid Question request body") 
public class QuestionInvalidException extends RuntimeException {
    public QuestionInvalidException() {
        super("Invalid request body for Question");
    }
}