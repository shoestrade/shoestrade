package com.study.shoestrade.exception.payment;

public class PaymentUnpaidException extends RuntimeException{
    public PaymentUnpaidException() {
        super();
    }

    public PaymentUnpaidException(String message) {
        super(message);
    }

    public PaymentUnpaidException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentUnpaidException(Throwable cause) {
        super(cause);
    }

    protected PaymentUnpaidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
