package com.study.shoestrade.exception.payment;

public class InsufficientPointException extends RuntimeException{
    public InsufficientPointException() {
        super();
    }

    public InsufficientPointException(String message) {
        super(message);
    }

    public InsufficientPointException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientPointException(Throwable cause) {
        super(cause);
    }

    protected InsufficientPointException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
