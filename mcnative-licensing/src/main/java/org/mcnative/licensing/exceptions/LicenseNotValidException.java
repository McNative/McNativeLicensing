package org.mcnative.licensing.exceptions;

/**
 * This exception is thrown when a license is invalid (e.g. Expired, invalid signature etc.)
 */
public class LicenseNotValidException extends RuntimeException{

    public LicenseNotValidException() {
        super("Resource license is not valid");
    }

    public LicenseNotValidException(Exception exception) {
        super(exception.getMessage(),exception);
    }
}
