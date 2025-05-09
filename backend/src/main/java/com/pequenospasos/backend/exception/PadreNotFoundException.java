package com.pequenospasos.backend.exception;

public class PadreNotFoundException extends RuntimeException {

    public PadreNotFoundException(String message) {
        super(message);
    }

    public PadreNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}