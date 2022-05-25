package com.study.shoestrade.exception.payment;

public class PaymentOrderIdNotConsistException extends RuntimeException{
    public PaymentOrderIdNotConsistException() {
        super();
    }

    public PaymentOrderIdNotConsistException(String message) {
        super(message);
    }

    public PaymentOrderIdNotConsistException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentOrderIdNotConsistException(Throwable cause) {
        super(cause);
    }

    protected PaymentOrderIdNotConsistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
