package com.study.shoestrade.exception;

public class BrandDuplicationException extends RuntimeException{

    public BrandDuplicationException() {
    }

    public BrandDuplicationException(String message) {
        super(message);
    }

    public BrandDuplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrandDuplicationException(Throwable cause) {
        super(cause);
    }

    public BrandDuplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
