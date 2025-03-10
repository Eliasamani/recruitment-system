package se.kth.iv1201.recruitment.model.exception;

/**
 * Thrown when there are no cookies in the request
 */
public class NoCookiesInRequestException extends RuntimeException {
    
    public NoCookiesInRequestException() {
    }

    public NoCookiesInRequestException(String message) {
        super(message);

    }
}
