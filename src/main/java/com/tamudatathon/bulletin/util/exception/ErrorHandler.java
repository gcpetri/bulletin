package com.tamudatathon.bulletin.util.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RecordNotFoundException handleRecordNotFoundException(RecordNotFoundException ce) {
        return ce;
    }

    @ExceptionHandler(RecordFormatInvalidException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public RecordFormatInvalidException handleRecordFormatInvalidException(RecordFormatInvalidException ce) {
        return ce;
    }

    @ExceptionHandler(S3Exception.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public S3Exception handleS3Exception(S3Exception ce) {
        return ce;
    }

    @ExceptionHandler(EditingForbiddenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public EditingForbiddenException handleEditingForbiddenException(EditingForbiddenException ce) {
        return ce;
    }

    @ExceptionHandler(LoginRedirectException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public LoginRedirectException handleLoginRedirectException(LoginRedirectException ce) {
        return ce;
    }
}
