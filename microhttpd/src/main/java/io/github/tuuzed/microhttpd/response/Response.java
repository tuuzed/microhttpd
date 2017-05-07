package io.github.tuuzed.microhttpd.response;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface Response extends Closeable {

    void setStatus(Status status);

    void setContentType(String contentType);

    void addHeader(String key, String value);

    void renderHtml(String html) throws IOException;

    void renderText(String text) throws IOException;

    void renderJson(String text) throws IOException;

    void renderXml(String text) throws IOException;

    void renderFile(File file) throws IOException;

    void renderError(Status status) throws IOException;

    void renderError(Status status, String errMsg) throws IOException;
}
