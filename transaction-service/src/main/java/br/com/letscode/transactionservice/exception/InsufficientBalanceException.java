package br.com.letscode.transactionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InsufficientBalanceException extends RuntimeException {

    private static final long serialVersionUID = -5412456629479401040L;

    public InsufficientBalanceException(String s) {super(s);}

}
