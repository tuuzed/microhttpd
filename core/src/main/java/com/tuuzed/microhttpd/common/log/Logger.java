package com.tuuzed.microhttpd.common.log;

public interface Logger {
    void debug(String message);

    void debug(String pattern, Object... args);

    void debug(String pattern, Throwable throwable, Object... args);

    void error(String message);

    void error(String pattern, Object... args);

    void error(String pattern, Throwable throwable, Object... args);
}
