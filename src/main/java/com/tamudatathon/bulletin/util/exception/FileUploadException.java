package com.tamudatathon.bulletin.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.SERVICE_UNAVAILABLE, reason="File Upload Failed") 
public class FileUploadException extends RuntimeException {
    public FileUploadException(String message) {
        super("File upload failed: " + message);
    }
}
