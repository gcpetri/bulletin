package com.tamudatathon.bulletin.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.SERVICE_UNAVAILABLE, reason="File Deletion Failed") 
public class FileDeleteException extends RuntimeException {
    public FileDeleteException(String message) {
        super("File deletion failed: " + message);
    }
}
