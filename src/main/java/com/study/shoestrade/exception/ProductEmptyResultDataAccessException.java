package com.study.shoestrade.exception;

import org.springframework.dao.EmptyResultDataAccessException;

public class ProductEmptyResultDataAccessException extends EmptyResultDataAccessException {
    public ProductEmptyResultDataAccessException(int expectedSize) {
        super(expectedSize);
    }

    public ProductEmptyResultDataAccessException(String msg, int expectedSize) {
        super(msg, expectedSize);
    }

    public ProductEmptyResultDataAccessException(String msg, int expectedSize, Throwable ex) {
        super(msg, expectedSize, ex);
    }

}