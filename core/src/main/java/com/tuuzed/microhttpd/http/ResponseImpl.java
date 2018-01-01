package com.tuuzed.microhttpd.http;

import com.tuuzed.microhttpd.common.util.CloseableUtils;
import com.tuuzed.microhttpd.view.file.MimeType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP响应
 */
public class ResponseImpl implements Response {
    private Status mStatus;
    private Map<String, String> mHeader;
    private Socket mConnection;
    private OutputStream mOutput;

    @NotNull
    public static Response create(@NotNull Socket connection) {
        return new ResponseImpl(connection);
    }

    private ResponseImpl(Socket connection) {
        mConnection = connection;
        mHeader = new HashMap<>();
        mStatus = Status.STATUS_200;
        putHeader("Server", "micro-httpd");
    }

    @Override
    public void setStatus(Status mStatus) {
        this.mStatus = mStatus;
    }

    @Override
    public void setContentType(String contentType) {
        putHeader("Content-Type", contentType);
    }

    @Override
    public void putHeader(String key, String value) {
        mHeader.put(key, value);
    }

    @Override
    public void renderHtml(String html) throws IOException {
        render(mStatus, "text/html; charset=utf-8", html.getBytes("utf-8"));
    }

    @Override
    public void renderText(String text) throws IOException {
        render(mStatus, "text/plain; charset=utf-8", text.getBytes("utf-8"));
    }

    @Override
    public void renderJson(String text) throws IOException {
        render(mStatus, "application/json; charset=utf-8", text.getBytes("utf-8"));
    }

    @Override
    public void renderXml(String text) throws IOException {
        render(mStatus, "text/xml; charset=utf-8", text.getBytes("utf-8"));
    }

    @Override
    public void renderArrayByte(byte[] bytes) throws IOException {
        renderArrayByte(bytes, 0, bytes.length);
    }

    @Override
    public void renderArrayByte(byte[] bytes, int off, int len) throws IOException {
        render(mStatus, "application/octet-stream", bytes, off, len);
    }

    @Override
    public void renderFile(File file) throws IOException {
        setStatus(mStatus);
        putHeader("Content-Length", String.valueOf(file.length()));
        putHeader("Content-Disposition", "inline; filename=" + file.getName());
        setContentType(MimeType.getInstance().get(file));
        writeHeader();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = fis.read(bytes)) != -1) {
                writeBody(bytes, 0, len);
            }
            mOutput.flush();
        } finally {
            close();
            CloseableUtils.safeClose(fis);
        }
    }

    @Override
    public void renderError(Status status) throws IOException {
        renderError(status, status.toString());
    }

    @Override
    public void renderError(Status status, String error) throws IOException {
        render(status, "text/plain charset=utf-8", error.getBytes("utf-8"));
    }

    @Override
    public void close() throws IOException {
        CloseableUtils.safeClose(mOutput);
        CloseableUtils.safeClose(mConnection);
    }

    private void render(Status status, String contentType, byte[] body) throws IOException {
        render(status, contentType, body, 0, body.length);
    }

    private void render(Status status, String contentType, byte[] body, int off, int len) throws IOException {
        setContentType(contentType);
        setStatus(status);
        putHeader("Content-Length", String.valueOf(body.length));
        putHeader("Date", new Date().toString());
        writeHeader();
        writeBody(body, off, len);
        mOutput.flush();
        close();
    }


    private void writeBody(byte[] bytes, int off, int len) throws IOException {
        mOutput.write(bytes, off, len);
    }

    // 写入头部
    private void writeHeader() throws IOException {
        mOutput = mConnection.getOutputStream();
        mOutput.write((String.format("HTTP/1.1 %s\r\n", mStatus.toString())).getBytes());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : mHeader.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        mOutput.write(sb.toString().getBytes());
        mOutput.write("\r\n".getBytes());
        mOutput.flush();
    }
}
