package br.com.letscode.userservice.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("User ID already exists.");
    }

}
