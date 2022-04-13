package com.study.shoestrade.exception.address;

public class BaseAddressNotDeleteException extends IllegalArgumentException{
    public BaseAddressNotDeleteException() {
        super();
    }

    public BaseAddressNotDeleteException(String s) {
        super(s);
    }

    public BaseAddressNotDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseAddressNotDeleteException(Throwable cause) {
        super(cause);
    }
}
