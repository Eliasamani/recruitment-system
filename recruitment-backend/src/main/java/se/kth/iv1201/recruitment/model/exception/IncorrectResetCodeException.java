package se.kth.iv1201.recruitment.model.exception;

public class IncorrectResetCodeException extends RuntimeException {
    public IncorrectResetCodeException() {
    }

    public IncorrectResetCodeException(String message) {
        super(message);

    }
}
