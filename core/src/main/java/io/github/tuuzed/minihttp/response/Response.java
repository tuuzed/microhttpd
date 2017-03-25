package io.github.tuuzed.minihttp.response;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface Response extends Closeable {

    void write(InputStream in) throws IOException;

    void write(byte[] bytes) throws IOException;

    void write(String str) throws IOException;

    void write(File file) throws IOException;

    void setStatus(Status status);

    void setContentType(String contentType);

    void addHeader(String key, String value);
}
