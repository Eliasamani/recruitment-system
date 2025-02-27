package se.kth.iv1201.recruitment.model.exception;

/**
 * Thrown when the provided reset code is either invalid, empty or expired
 */
public class IncorrectResetCodeException extends RuntimeException {

    public IncorrectResetCodeException() {
    }

    public IncorrectResetCodeException(String message) {
        super(message);

    }
}
