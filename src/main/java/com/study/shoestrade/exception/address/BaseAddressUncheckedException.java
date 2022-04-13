package com.study.shoestrade.exception.address;

public class BaseAddressUncheckedException extends IllegalArgumentException{
    public BaseAddressUncheckedException() {
        super();
    }

    public BaseAddressUncheckedException(String s) {
        super(s);
    }

    public BaseAddressUncheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseAddressUncheckedException(Throwable cause) {
        super(cause);
    }
}
