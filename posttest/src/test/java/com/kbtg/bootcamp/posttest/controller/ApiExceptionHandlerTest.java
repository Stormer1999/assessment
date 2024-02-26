package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.dto.status.ResponseStatus;
import com.kbtg.bootcamp.posttest.exception.BadRequestException;
import com.kbtg.bootcamp.posttest.exception.DatabaseErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerTest {

    @InjectMocks
    private ApiExceptionHandler apiExceptionHandler;

    @Test
    void testHandleMethodArgumentNotValidException() {
        // arrange
        FieldError fieldError = mock(FieldError.class);
        when(fieldError.getDefaultMessage()).thenReturn("Invalid field");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldError()).thenReturn(fieldError);

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        // act
        ResponseEntity<Object> responseEntity = apiExceptionHandler.handleMethodArgumentNotValidException(ex);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid field",
                ((ResponseStatus) Objects.requireNonNull(responseEntity.getBody())).message());
    }

    @Test
    void testHandleBadRequestException() {
        // arrange
        BadRequestException ex = new BadRequestException("Bad request");

        // act
        ResponseEntity<Object> responseEntity = apiExceptionHandler.handleBadRequestException(ex);

        // assert
        String expectedMessage = "Bad request";
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(expectedMessage, ((ResponseStatus) Objects.requireNonNull(responseEntity.getBody())).message());
    }

    @Test
    void testHandleDatabaseErrorException() {
        // arrange
        DatabaseErrorException ex = new DatabaseErrorException("Database error");

        // act
        ResponseEntity<Object> responseEntity = apiExceptionHandler.handleDatabaseErrorException(ex);

        // assert
        String expectedMessage = "Database error";
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(expectedMessage, ((ResponseStatus) Objects.requireNonNull(responseEntity.getBody())).message());
    }
}