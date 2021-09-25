package br.com.letscode.transactionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class WrongAmountException extends RuntimeException {

    private static final long serialVersionUID = -4221862769676704440L;

    public WrongAmountException(String s) {
        super(s);
    }

}

