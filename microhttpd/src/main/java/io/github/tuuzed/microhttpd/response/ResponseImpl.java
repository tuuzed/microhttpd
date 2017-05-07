package io.github.tuuzed.microhttpd.response;

import io.github.tuuzed.microhttpd.staticfile.MimeType;
import io.github.tuuzed.microhttpd.util.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP响应
 */
public class ResponseImpl implements Response {
    private static final Logger sLogger = Logger.getLogger(ResponseImpl.class);
    private Status mStatus;
    private Map<String, String> mHeader;
    private Socket mConnect;
    private OutputStream mOut;

    public ResponseImpl(Socket connect) {
        mConnect = connect;
        mHeader = new HashMap<>();
        mStatus = Status.STATUS_200;
        addHeader("Server", "MicroHTTPd");
    }


    @Override
    public void setStatus(Status mStatus) {
        this.mStatus = mStatus;
    }

    @Override
    public void setContentType(String contentType) {
        addHeader("Content-Type", contentType);
    }

    @Override
    public void addHeader(String key, String value) {
        mHeader.put(key, value);
    }

    @Override
    public void renderHtml(String html) throws IOException {
        render(mStatus, "text/html; charset=utf-8",
                html.getBytes("utf-8"));
    }

    @Override
    public void renderText(String text) throws IOException {
        render(mStatus, "text/plain; charset=utf-8",
                text.getBytes("utf-8"));
    }

    @Override
    public void renderJson(String text) throws IOException {
        render(mStatus, "application/json; charset=utf-8",
                text.getBytes("utf-8"));
    }

    @Override
    public void renderXml(String text) throws IOException {
        render(mStatus, "text/xml; charset=utf-8",
                text.getBytes("utf-8"));
    }

    @Override
    public void renderFile(File file) throws IOException {
        setStatus(mStatus);
        addHeader("Content-Length", String.valueOf(file.length()));
        addHeader("Content-Disposition", "inline; filename=" + file.getName());
        setContentType(MimeType.getMimeType(file));
        writeHeader();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = fis.read(bytes)) != -1) {
                write(bytes, 0, len);
            }
        } catch (IOException e) {
            sLogger.e(e);
            throw e;
        } finally {
            close();
            safeClose(fis);
        }
    }

    @Override
    public void renderError(Status status) throws IOException {
        renderError(status, status.toString());
    }

    @Override
    public void renderError(Status status, String errMsg) throws IOException {
        render(status, "text/plain charset=utf-8",
                errMsg.getBytes("utf-8"));
    }

    @Override
    public void close() throws IOException {
        safeClose(mOut);
        safeClose(mConnect);
    }

    private void render(Status status, String contentType, byte[] body) throws IOException {
        setContentType(contentType);
        setStatus(status);
        addHeader("Content-Length", String.valueOf(body.length));
        addHeader("Date", new Date().toString());
        try {
            writeHeader();
            write(body);
        } catch (IOException e) {
            sLogger.e(e);
            throw e;
        } finally {
            close();
        }
    }

    private void write(byte[] bytes) throws IOException {
        write(bytes, 0, bytes.length);
    }

    private void write(byte[] bytes, int off, int len) throws IOException {
        mOut.write(bytes, off, len);
    }

    // 写入头部
    private void writeHeader() throws IOException {
        mOut = mConnect.getOutputStream();
        mOut.write((String.format("HTTP/1.1 %s\r\n", mStatus.toString())).getBytes());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : mHeader.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        mOut.write(sb.toString().getBytes());
        mOut.write("\r\n".getBytes());
    }

    private void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                sLogger.e(e);
            }
        }
    }
}
