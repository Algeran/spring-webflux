package com.springfreamwork.webflux.com.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandling(Exception ex) {
        return ResponseEntity.status(501)
                .body(ex.getMessage());
    }
}
