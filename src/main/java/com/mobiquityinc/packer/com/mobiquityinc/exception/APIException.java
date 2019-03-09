package com.mobiquityinc.packer.com.mobiquityinc.exception;

/**
 * This exception should be thrown when incorrect parameters are being passed.
 */
public class APIException extends Exception {
    public APIException() {
        super();
    }

    public APIException(String message) {
        super(message);
    }

    public APIException(String message, Throwable cause) {
        super(message, cause);
    }

    public APIException(Throwable cause) {
        super(cause);
    }
}
