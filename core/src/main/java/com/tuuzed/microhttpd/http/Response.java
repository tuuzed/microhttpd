package com.tuuzed.microhttpd.http;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public interface Response extends Closeable {

    void setStatus(Status status);

    void setContentType(String contentType);

    void putHeader(String key, String value);

    void renderHtml(String html) throws IOException;

    void renderText(String text) throws IOException;

    void renderJson(String text) throws IOException;

    void renderXml(String text) throws IOException;

    void renderArrayByte(byte[] bytes) throws IOException;

    void renderArrayByte(byte[] bytes, int off, int len) throws IOException;

    void renderFile(File file) throws IOException;

    void renderError(Status status) throws IOException;

    void renderError(Status status, String error) throws IOException;
}
