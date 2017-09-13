package com.tuuzed.microhttpd.http;

import org.jetbrains.annotations.Nullable;

public interface Request {
    String getMethod();

    String getUrl();

    Protocol getProtocol();

    String getHeader(String key);

    String getQueryParam(String key);

    @Nullable
    byte[] getData();
}
