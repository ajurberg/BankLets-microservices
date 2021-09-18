package br.com.letcode.accountservice.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException() {
        super("Insufficient balance. Operation not performed.");
    }

}
