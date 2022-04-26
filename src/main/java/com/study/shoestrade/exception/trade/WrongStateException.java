package com.study.shoestrade.exception.trade;

public class WrongStateException extends RuntimeException{
    public WrongStateException() {
        super();
    }

    public WrongStateException(String message) {
        super(message);
    }

    public WrongStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongStateException(Throwable cause) {
        super(cause);
    }

    protected WrongStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
