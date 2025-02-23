package se.kth.iv1201.recruitment.model.exception;

public class UserServiceException extends RuntimeException {
    public UserServiceException() {
    }

    public UserServiceException(String message) {
        super(message);

    }

}
