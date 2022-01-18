package com.tamudatathon.bulletin.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="No such Submission") 
public class SubmissionNotFoundException extends RuntimeException {
    public SubmissionNotFoundException(Long id) {
        super("Could not find Submission with id " + id);
    }
}