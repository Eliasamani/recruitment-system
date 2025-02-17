package se.kth.iv1201.recruitment.model.exception;

public class NonExistingEmailException extends RuntimeException {
    public NonExistingEmailException() {
    }

    public NonExistingEmailException(String message) {
        super(message);

    }
}
