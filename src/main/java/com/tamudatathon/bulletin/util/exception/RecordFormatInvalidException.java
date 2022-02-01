package com.tamudatathon.bulletin.util.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid record format") 
public class RecordFormatInvalidException extends RuntimeException {
    public RecordFormatInvalidException(String tableName) {
        super("Invalid request body. Could not create record in " + tableName + " given with the request");
    }
}
