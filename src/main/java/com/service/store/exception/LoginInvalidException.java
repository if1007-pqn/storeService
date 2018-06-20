package com.service.store.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class LoginInvalidException extends RuntimeException {
    public LoginInvalidException() {
        super("Username or Password invalid");
    }

}