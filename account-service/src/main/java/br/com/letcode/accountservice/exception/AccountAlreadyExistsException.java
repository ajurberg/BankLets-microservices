package br.com.letcode.accountservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AccountAlreadyExistsException extends RuntimeException {

    public AccountAlreadyExistsException() {
        super("Account ID already exists.");
    }

}
