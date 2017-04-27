package io.github.tuuzed.microhttpd.response;

import io.github.tuuzed.microhttpd.util.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP响应
 */
public class ResponseImpl implements Response {
    private static final Logger sLogger = Logger.getLogger(ResponseImpl.class);
    // 是否写入头部
    private boolean isWriteHeader;
    private Status mStatus;
    private Map<String, String> mHeader;
    private Socket mClient;
    private OutputStream mOut;

    public ResponseImpl(Socket client) {
        mClient = client;
        isWriteHeader = false;
        mHeader = new HashMap<>();
        mStatus = Status.STATUS_200;
        addHeader("Server", "MicroHTTPd");
    }

    @Override
    public void write(InputStream in) throws IOException {
        write(in, 1024);
    }

    @Override
    public void write(InputStream in, int bufSize) throws IOException {
        // 检查客户端是否已经断开连接
        if (mClient.isClosed()) {
            safeClose(in);
            return;
        }
        byte[] bytes = new byte[bufSize];
        while (in.available() != 0) {
            int read = in.read(bytes);
            write(bytes, 0, read);
        }
        safeClose(in);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        write(bytes, 0, bytes.length);
    }

    @Override
    public void write(byte[] bytes, int off, int len) throws IOException {
        if (mClient.isClosed()) return;
        writeHeader();
        mOut.write(bytes, off, len);
    }

    @Override
    public void write(String str) throws IOException {
        write(str, "utf-8");
    }

    @Override
    public void write(String str, String charsetName) throws IOException {
        if (mClient.isClosed()) return;
        write(str.getBytes(charsetName));
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
    public void close() throws IOException {
        safeClose(mOut);
        safeClose(mClient);
    }

    // 写入头部
    private void writeHeader() throws IOException {
        // 检查客户端是否已经断开连接
        if (mClient.isClosed()) return;
        mOut = mClient.getOutputStream();
        if (!isWriteHeader) {
            mOut.write((String.format("HTTP/1.1 %s\r\n", mStatus.toString())).getBytes());
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : mHeader.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
            }
            mOut.write(sb.toString().getBytes());
            mOut.write("\r\n".getBytes());
            isWriteHeader = true;
        }
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
