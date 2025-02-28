package se.kth.iv1201.recruitment.model.exception;

/**
 * Thrown when the application is not found
 */
public class ApplicationNotFoundException extends RuntimeException{
    public ApplicationNotFoundException() {
    }

    public ApplicationNotFoundException(String message) {
        super(message);

    }
}

