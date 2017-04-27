package io.github.tuuzed.microhttpd.response;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface Response extends Closeable {

    void write(InputStream in) throws IOException;

    void write(InputStream in, int bufSize) throws IOException;

    void write(byte[] bytes) throws IOException;

    void write(byte[] bytes, int off, int len) throws IOException;

    void write(String str) throws IOException;

    void write(String str, String charsetName) throws IOException;

    void setStatus(Status status);

    void setContentType(String contentType);

    void addHeader(String key, String value);
}
