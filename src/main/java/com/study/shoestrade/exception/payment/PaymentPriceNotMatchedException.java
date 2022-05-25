package com.study.shoestrade.exception.payment;

public class PaymentPriceNotMatchedException extends RuntimeException{
    public PaymentPriceNotMatchedException() {
        super();
    }

    public PaymentPriceNotMatchedException(String message) {
        super(message);
    }

    public PaymentPriceNotMatchedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentPriceNotMatchedException(Throwable cause) {
        super(cause);
    }

    protected PaymentPriceNotMatchedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
