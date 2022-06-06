package com.study.shoestrade.exception.payment;

public class PaymentCancelFailureException extends RuntimeException{
    public PaymentCancelFailureException() {
        super();
    }

    public PaymentCancelFailureException(String message) {
        super(message);
    }

    public PaymentCancelFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentCancelFailureException(Throwable cause) {
        super(cause);
    }

    protected PaymentCancelFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
