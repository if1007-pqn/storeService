package com.service.store.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)

public class InvalidJsonException extends RuntimeException {
    public InvalidJsonException() {
        super("Malformed Json");
    }

}
