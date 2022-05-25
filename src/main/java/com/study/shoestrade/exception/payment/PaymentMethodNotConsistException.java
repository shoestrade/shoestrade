package com.study.shoestrade.exception.payment;

public class PaymentMethodNotConsistException extends RuntimeException{
    public PaymentMethodNotConsistException() {
        super();
    }

    public PaymentMethodNotConsistException(String message) {
        super(message);
    }

    public PaymentMethodNotConsistException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentMethodNotConsistException(Throwable cause) {
        super(cause);
    }

    protected PaymentMethodNotConsistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
