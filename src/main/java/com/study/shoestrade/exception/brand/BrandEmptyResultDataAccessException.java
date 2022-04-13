package com.study.shoestrade.exception.brand;

import org.springframework.dao.EmptyResultDataAccessException;

public class BrandEmptyResultDataAccessException extends EmptyResultDataAccessException {
    public BrandEmptyResultDataAccessException(int expectedSize) {
        super(expectedSize);
    }

    public BrandEmptyResultDataAccessException(String msg, int expectedSize) {
        super(msg, expectedSize);
    }

    public BrandEmptyResultDataAccessException(String msg, int expectedSize, Throwable ex) {
        super(msg, expectedSize, ex);
    }

}