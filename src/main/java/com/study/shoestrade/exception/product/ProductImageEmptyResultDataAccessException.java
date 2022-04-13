package com.study.shoestrade.exception.product;

import org.springframework.dao.EmptyResultDataAccessException;

public class ProductImageEmptyResultDataAccessException extends EmptyResultDataAccessException {
    public ProductImageEmptyResultDataAccessException(int expectedSize) {
        super(expectedSize);
    }

    public ProductImageEmptyResultDataAccessException(String msg, int expectedSize) {
        super(msg, expectedSize);
    }

    public ProductImageEmptyResultDataAccessException(String msg, int expectedSize, Throwable ex) {
        super(msg, expectedSize, ex);
    }

}