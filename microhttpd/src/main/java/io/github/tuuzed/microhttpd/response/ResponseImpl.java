package io.github.tuuzed.microhttpd.response;

import io.github.tuuzed.microhttpd.util.CloseableUtils;
import io.github.tuuzed.microhttpd.util.Logger;
import io.github.tuuzed.microhttpd.util.MimeType;

import java.io.*;
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
        // 检查客户端是否已经断开连接
        if (mClient.isClosed()) return;
        byte[] bytes;
        int available = in.available();
        if (available < 1024) {
            bytes = new byte[available];
        } else {
            bytes = new byte[1024];
        }
        while ((available = in.available()) != 0) {
            if (available < 1024) {
                bytes = new byte[available];
            }
            int read = in.read(bytes);
            write(bytes);
        }
        in.close();
    }

    @Override
    public void write(File file) throws IOException {
        // 检查客户端是否已经断开连接
        if (mClient.isClosed()) return;
        if (!file.exists()) {
            // 文件不存在
            setStatus(Status.STATUS_404);
            write(Status.STATUS_404.toString());
        } else if (file.isDirectory()) {
            // 是一个文件夹
            setStatus(Status.STATUS_403);
            write(Status.STATUS_403.toString());
        } else {
            setContentType(MimeType.getMimeType(file));
            addHeader("Content-Disposition", "filename=" + file.getName());
            setStatus(Status.STATUS_200);
            if (file.length() > 0) {
                write(new FileInputStream(file));
            } else {
                write("");
            }
        }
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        // 检查客户端是否已经断开连接
        if (mClient.isClosed()) return;
        writeHeader();
        mOut.write(bytes);
    }

    @Override
    public void write(String str) throws IOException {
        // 检查客户端是否已经断开连接
        if (mClient.isClosed()) return;
        write(str.getBytes());
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
    public void close() throws IOException {
        CloseableUtils.quietClose(mOut);
        CloseableUtils.quietClose(mClient);
    }

    /**
     * 添加头部信息
     *
     * @param key
     * @param value
     */
    @Override
    public void addHeader(String key, String value) {
        mHeader.put(key, value);
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
}
