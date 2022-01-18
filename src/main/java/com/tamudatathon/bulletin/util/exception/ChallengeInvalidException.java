package com.tamudatathon.bulletin.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid Challenge request body") 
public class ChallengeInvalidException extends RuntimeException {
    public ChallengeInvalidException(String message) {
        super("Invalid request body for challenge: " + message);
    }
}
