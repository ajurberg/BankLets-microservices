package br.com.letscode.transactionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FAILED_DEPENDENCY)
public class FailedDependencyException extends RuntimeException {

    public FailedDependencyException(String s) {
        super(s);
    }

}
