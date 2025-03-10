package se.kth.iv1201.recruitment.model.exception;

/**
 * Thrown when the status value is invalid
 */
public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException(String invalidValue) {
        super("Invalid status value: " + invalidValue);
    }
}
