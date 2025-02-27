package se.kth.iv1201.recruitment.model.exception;

/**
 * Thrown when the user provided email cannot be found within the system's
 * database
 */
public class NonExistingEmailException extends RuntimeException {

    public NonExistingEmailException() {
    }

    public NonExistingEmailException(String message) {
        super(message);

    }
}
