package br.com.letcode.accountservice.exception;

public class CloseAccountBalanceException extends RuntimeException {

    public CloseAccountBalanceException() {
        super("Account balance must be ZERO to close an account. Operation not performed.");
    }

}
