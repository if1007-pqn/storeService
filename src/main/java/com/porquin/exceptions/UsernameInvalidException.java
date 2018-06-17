package com.porquin.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class UsernameInvalidException extends RuntimeException {
    public UsernameInvalidException() {
        super("Try another username");
    }

}