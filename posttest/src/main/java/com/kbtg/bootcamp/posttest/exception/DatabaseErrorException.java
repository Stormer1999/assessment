package com.kbtg.bootcamp.posttest.exception;

public class DatabaseErrorException extends RuntimeException {

    public DatabaseErrorException(String message) {
        super(message);
    }
}
