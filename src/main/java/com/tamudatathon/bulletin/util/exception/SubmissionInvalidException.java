package com.tamudatathon.bulletin.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid Submission request body") 
public class SubmissionInvalidException extends RuntimeException {
    public SubmissionInvalidException(String message) {
        super("Invalid request body for Submission: " + message);
    }
}
