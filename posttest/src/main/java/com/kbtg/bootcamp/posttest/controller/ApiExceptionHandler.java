package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.constant.Response;
import com.kbtg.bootcamp.posttest.dto.status.ResponseStatus;
import com.kbtg.bootcamp.posttest.exception.BadRequestException;
import com.kbtg.bootcamp.posttest.exception.DatabaseErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "Validation error";

        return ResponseEntity.badRequest()
                .body(new ResponseStatus(Response.FAIL.getContent(), errorMessage));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.badRequest()
                .body(new ResponseStatus(Response.FAIL.getContent(), ex.getMessage()));
    }

    @ExceptionHandler(DatabaseErrorException.class)
    public ResponseEntity<Object> handleDatabaseErrorException(DatabaseErrorException ex) {
        return ResponseEntity.internalServerError()
                .body(new ResponseStatus(Response.FAIL.getContent(), ex.getMessage()));
    }
}
