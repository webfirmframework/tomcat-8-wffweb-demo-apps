package com.wffwebdemo.minimalproductionsample;

public class AppException extends Exception {

    private static final long serialVersionUID = 1L;

    public AppException() {
        super();
    }

    public AppException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(Throwable cause) {
        super(cause);
    }

}
