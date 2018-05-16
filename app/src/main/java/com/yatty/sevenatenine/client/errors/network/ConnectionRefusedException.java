package com.yatty.sevenatenine.client.errors.network;

import com.yatty.sevenatenine.client.errors.SevenAteNineException;

public class ConnectionRefusedException extends SevenAteNineException {
    public ConnectionRefusedException(String message) {
        super(message);
    }

    public ConnectionRefusedException(String message, Throwable cause) {
        super(message, cause);
    }
}
