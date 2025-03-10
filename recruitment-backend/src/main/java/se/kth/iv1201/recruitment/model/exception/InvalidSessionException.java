package se.kth.iv1201.recruitment.model.exception;

/**
 * Thrown when the session is unable to be validated
 */
public class InvalidSessionException extends RuntimeException {

    public InvalidSessionException() {
    }

    public InvalidSessionException(String message) {
        super(message);

    }
}
