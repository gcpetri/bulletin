package com.tamudatathon.bulletin.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="No such Challenge") 
public class ChallengeNotFoundException extends RuntimeException {
    public ChallengeNotFoundException(Long id) {
        super("Could not find challenge with id " + id);
    }
}
