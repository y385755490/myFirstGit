package com.ace.trade.common.exception;

public class AceOrderException extends RuntimeException{
    public AceOrderException() {
        super();
    }

    public AceOrderException(String message) {
        super(message);
    }

    public AceOrderException(Throwable cause) {
        super(cause);
    }
}
