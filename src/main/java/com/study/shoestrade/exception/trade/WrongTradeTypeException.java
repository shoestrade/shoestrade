package com.study.shoestrade.exception.trade;

public class WrongTradeTypeException extends RuntimeException{
    public WrongTradeTypeException() {
        super();
    }

    public WrongTradeTypeException(String message) {
        super(message);
    }

    public WrongTradeTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongTradeTypeException(Throwable cause) {
        super(cause);
    }

    protected WrongTradeTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
