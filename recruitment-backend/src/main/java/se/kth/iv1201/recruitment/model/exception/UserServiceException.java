package se.kth.iv1201.recruitment.model.exception;

/**
 * Thrown when non specific exceptions occurs in user service
 */
public class UserServiceException extends RuntimeException {
    public UserServiceException() {
    }

    public UserServiceException(String message) {
        super(message);

    }

}
