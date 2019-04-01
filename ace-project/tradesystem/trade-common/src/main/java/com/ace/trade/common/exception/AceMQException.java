package com.ace.trade.common.exception;

public class AceMQException extends Exception{
    public AceMQException() {
        super();
    }

    public AceMQException(String message) {
        super(message);
    }

    public AceMQException(String message, Throwable cause) {
        super(message, cause);
    }

    public AceMQException(Throwable cause) {
        super(cause);
    }
}
