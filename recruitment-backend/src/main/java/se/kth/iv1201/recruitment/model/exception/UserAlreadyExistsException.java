package se.kth.iv1201.recruitment.model.exception;

/**
 * Thrown when an user with the specified user information (eg. username)
 * already exists and thus cannot be created/modified with this info.
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
    }

    public UserAlreadyExistsException(String message) {
        super(message);

    }

}