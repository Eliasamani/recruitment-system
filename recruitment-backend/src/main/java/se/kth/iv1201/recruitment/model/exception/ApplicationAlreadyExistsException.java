package se.kth.iv1201.recruitment.model.exception;

/**
 * Thrown when the application for the user already exists
 */
public class ApplicationAlreadyExistsException extends RuntimeException{
    public ApplicationAlreadyExistsException() {
    }

    public ApplicationAlreadyExistsException(String message) {
        super(message);

    }
}

