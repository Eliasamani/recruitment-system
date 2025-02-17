package se.kth.iv1201.recruitment.model.exception;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException() {}

    public UserAlreadyExistsException(String message) {
        super(message);

    }
    
}