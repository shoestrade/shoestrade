package com.study.shoestrade.exception.payment;

public class PaymentRestTemplateException extends RuntimeException{
    public PaymentRestTemplateException() {
        super();
    }

    public PaymentRestTemplateException(String message) {
        super(message);
    }

    public PaymentRestTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentRestTemplateException(Throwable cause) {
        super(cause);
    }

    protected PaymentRestTemplateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
