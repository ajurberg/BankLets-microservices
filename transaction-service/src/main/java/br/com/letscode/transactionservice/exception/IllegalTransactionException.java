package br.com.letscode.transactionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class IllegalTransactionException extends RuntimeException {

    private static final long serialVersionUID = -3119362145594178543L;

    public IllegalTransactionException(String s) {super(s);}

}
