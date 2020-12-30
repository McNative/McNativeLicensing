package org.mcnative.licensing.exceptions;

/**
 * This exception is thrown when a license is invalid (e.g. Expired, invalid signature etc.)
 */
public class CloudNotCheckoutLicenseException extends RuntimeException{

    public CloudNotCheckoutLicenseException() {
        super("Cloud license not checkout");
    }

    public CloudNotCheckoutLicenseException(String message) {
        super(message);
    }
}
