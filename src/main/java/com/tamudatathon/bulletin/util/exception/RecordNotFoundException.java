package com.tamudatathon.bulletin.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="No record found") 
public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String tableName, Long id) {
        super("Could not find record from " + tableName + " with id " + id);
    }
}
