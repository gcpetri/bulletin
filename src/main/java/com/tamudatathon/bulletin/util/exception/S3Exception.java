package com.tamudatathon.bulletin.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.SERVICE_UNAVAILABLE, reason="S3 Failed") 
public class S3Exception extends RuntimeException {
    public S3Exception(String verb, String message) {
        super("Could not " + verb + " file. Response: " + message);
    }
}
