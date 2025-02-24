package se.kth.iv1201.recruitment.model.exception;

public class InvalidSessionException extends RuntimeException {

    public InvalidSessionException() {
    }

    public InvalidSessionException(String message) {
        super(message);

    }
}
