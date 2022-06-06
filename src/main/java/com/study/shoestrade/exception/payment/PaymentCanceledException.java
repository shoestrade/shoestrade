package com.study.shoestrade.exception.payment;

public class PaymentCanceledException extends RuntimeException{
    public PaymentCanceledException() {
        super();
    }

    public PaymentCanceledException(String message) {
        super(message);
    }

    public PaymentCanceledException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentCanceledException(Throwable cause) {
        super(cause);
    }

    protected PaymentCanceledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
