package com.tuuzed.microhttpd.http;

import java.io.IOException;

public interface Request {
    String getMethod();

    String getUrl();

    String getProtocol();

    String getHeader(String key);

    String getParam(String key);

    byte[] getData() throws IOException;
}
