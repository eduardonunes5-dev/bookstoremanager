package com.eduardonunes.bookstoremanager.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class BookStoreExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception){
        return buildResponseEntity(HttpStatus.NOT_FOUND,
                exception.getMessage(),
                Collections.singletonList(exception.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(fieldError -> errors.add("Field " + fieldError.getField().toUpperCase() + " " + fieldError.getDefaultMessage()));
        exception.getBindingResult().getGlobalErrors()
                .forEach(globalError -> errors.add("Object " + globalError.getObjectName().toUpperCase() + " " + globalError.getDefaultMessage()));

        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Informed argument(s) error(s)", errors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST,
                "Malformed JSON body and/or fieldError", Collections.singletonList(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Object> handleEntityExistsException(EntityExistsException exception){
        return buildResponseEntity(HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                Collections.singletonList(exception.getMessage()));
    }


    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String msg, List<String> errors){
        CustomErrorResponse apiError = CustomErrorResponse.builder()
                .code(status.value())
                .status(status.getReasonPhrase())
                .message(msg)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, status);
    }

}
