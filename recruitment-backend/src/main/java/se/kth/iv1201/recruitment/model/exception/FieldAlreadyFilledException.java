package se.kth.iv1201.recruitment.model.exception;

/**
 * Thrown when the user tries to fill a field that is already filled
 */
public class FieldAlreadyFilledException extends RuntimeException {

    public FieldAlreadyFilledException() {
    }

    public FieldAlreadyFilledException(String message) {
        super(message);
    }

    
}