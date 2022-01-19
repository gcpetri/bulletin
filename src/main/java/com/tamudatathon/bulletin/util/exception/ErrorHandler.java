package com.tamudatathon.bulletin.util.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(AccoladeNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AccoladeNotFoundException handleAccoladeNotFoundException(AccoladeNotFoundException ce) {
        return ce;
    }

    @ExceptionHandler(ChallengeNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ChallengeNotFoundException handleChallengeNotFoundException(ChallengeNotFoundException ce) {
        return ce;
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EventNotFoundException handleEventNotFoundException(EventNotFoundException ce) {
        return ce;
    }

    @ExceptionHandler(SubmissionNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SubmissionNotFoundException handleSubmissionNotFoundException(SubmissionNotFoundException ce) {
        return ce;
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public QuestionNotFoundException handleQuestionNotFoundException(QuestionNotFoundException ce) {
        return ce;
    }

    @ExceptionHandler(AccoladeInvalidException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public AccoladeInvalidException handleAccoladeInvalidException(AccoladeInvalidException ce) {
        return ce;
    }

    @ExceptionHandler(ChallengeInvalidException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ChallengeInvalidException handleChallengeInvalidException(ChallengeInvalidException ce) {
        return ce;
    }

    @ExceptionHandler(EventInvalidException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public EventInvalidException handleEventInvalidException(EventInvalidException ce) {
        return ce;
    }

    @ExceptionHandler(SubmissionInvalidException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public SubmissionInvalidException handleSubmissionInvalidException(SubmissionInvalidException ce) {
        return ce;
    }

    @ExceptionHandler(QuestionInvalidException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public QuestionInvalidException handleQuestionInvalidException(QuestionInvalidException ce) {
        return ce;
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public FileUploadException handleFileUploadException(FileUploadException ce) {
        return ce;
    }

    @ExceptionHandler(FileDeleteException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public FileDeleteException handleFileDeleteException(FileDeleteException ce) {
        return ce;
    }
}
