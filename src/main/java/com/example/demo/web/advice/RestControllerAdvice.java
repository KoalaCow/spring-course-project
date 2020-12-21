package com.example.demo.web.advice;

import com.example.demo.exceptions.InvalidEntityException;
import com.example.demo.exceptions.InvalidOperationException;
import com.example.demo.web.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestControllerAdvice {
    @ExceptionHandler({InvalidEntityException.class, InvalidOperationException.class})
    public final ResponseEntity<ErrorDetails> handleException(Exception e, WebRequest request) {
        return new ResponseEntity<>(new ErrorDetails(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
