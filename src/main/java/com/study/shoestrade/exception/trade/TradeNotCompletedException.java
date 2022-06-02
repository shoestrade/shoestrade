package com.study.shoestrade.exception.trade;

public class TradeNotCompletedException extends RuntimeException{
    public TradeNotCompletedException() {
        super();
    }

    public TradeNotCompletedException(String message) {
        super(message);
    }

    public TradeNotCompletedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TradeNotCompletedException(Throwable cause) {
        super(cause);
    }

    protected TradeNotCompletedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
