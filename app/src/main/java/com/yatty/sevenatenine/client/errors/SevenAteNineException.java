package com.yatty.sevenatenine.client.errors;

public class SevenAteNineException extends RuntimeException {
    public SevenAteNineException() {
    }

    public SevenAteNineException(String message) {
        super(message);
    }

    public SevenAteNineException(String message, Throwable cause) {
        super(message, cause);
    }

    public SevenAteNineException(Throwable cause) {
        super(cause);
    }

    public SevenAteNineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
