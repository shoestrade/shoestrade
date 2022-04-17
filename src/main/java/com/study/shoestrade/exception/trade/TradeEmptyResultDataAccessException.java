package com.study.shoestrade.exception.trade;

import org.springframework.dao.EmptyResultDataAccessException;

public class TradeEmptyResultDataAccessException extends EmptyResultDataAccessException {
    public TradeEmptyResultDataAccessException(int expectedSize) {
        super(expectedSize);
    }

    public TradeEmptyResultDataAccessException(String msg, int expectedSize) {
        super(msg, expectedSize);
    }

    public TradeEmptyResultDataAccessException(String msg, int expectedSize, Throwable ex) {
        super(msg, expectedSize, ex);
    }

}