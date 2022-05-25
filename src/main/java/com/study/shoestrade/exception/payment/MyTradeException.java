package com.study.shoestrade.exception.payment;

public class MyTradeException extends RuntimeException{
    public MyTradeException() {
        super();
    }

    public MyTradeException(String message) {
        super(message);
    }

    public MyTradeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyTradeException(Throwable cause) {
        super(cause);
    }

    protected MyTradeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
