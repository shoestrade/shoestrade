package com.study.shoestrade.exception.payment;

public class PaymentNotMatchedException extends RuntimeException{
    public PaymentNotMatchedException() {
        super();
    }

    public PaymentNotMatchedException(String message) {
        super(message);
    }

    public PaymentNotMatchedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentNotMatchedException(Throwable cause) {
        super(cause);
    }

    protected PaymentNotMatchedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
