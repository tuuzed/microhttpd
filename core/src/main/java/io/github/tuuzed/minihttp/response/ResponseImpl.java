package io.github.tuuzed.minihttp.response;

import io.github.tuuzed.minihttp.util.Logger;
import io.github.tuuzed.minihttp.util.MimeType;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * HTTP响应
 */
public class ResponseImpl implements Response {
    private static final Pattern sDefIndex = Pattern.compile("^((index)|(default))\\.((htm)|(html))$");
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
        addHeader("Server", "MiniHTTPd");
    }

    @Override
    public void write(InputStream in) throws IOException {
        byte[] bytes = new byte[1024];
        while (in.read(bytes) != -1) {
            write(bytes);
        }
    }

    @Override
    public void write(File file) throws IOException {
        if (!file.exists()) {
            // 如果文件不存在
            setStatus(Status.STATUS_404);
            write(Status.STATUS_404.toString().getBytes());
            throw new FileNotFoundException(String.format("not find %s%s%s", file.getAbsolutePath(), File.separator, file.getName()));
        } else if (file.isDirectory()) {
            // 是一个文件夹
            String[] list = file.list();
            if (list == null) {
                setStatus(Status.STATUS_404);
                write(Status.STATUS_404.toString().getBytes());
                throw new FileNotFoundException(String.format("not find %s%s%s", file.getAbsolutePath(), File.separator, file.getName()));
            } else {
                for (String s : list) {
                    if (sDefIndex.matcher(s).find()) {
                        sLogger.d("获取到默认首页HTML文件:" + s);
                        file = new File(file, s);
                        break;
                    }
                }
                if (file.isDirectory()) {
                    // 还是一个目录
                    setStatus(Status.STATUS_404);
                    write(Status.STATUS_404.toString().getBytes());
                    throw new FileNotFoundException("not find default index file");
                } else {
                    // 是一个文件
                    addHeader("Content-Type", MimeType.getMimeType(file));
                    addHeader("Content-Disposition", "filename=" + file.getName());
                    setStatus(Status.STATUS_200);
                    write(new FileInputStream(file));
                }
            }
        }
    }


    @Override
    public void write(byte[] bytes) throws IOException {
        writeHeader();
        mOut.write(bytes);
    }

    @Override
    public void write(String str) throws IOException {
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
    public void finish() {
        InputStream inputStream = null;
        try {
            inputStream = mClient.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            quietClose(inputStream);
            quietClose(mOut);
            quietClose(mClient);
        }
    }

    // 写入头部
    private void writeHeader() throws IOException {
        mOut = mClient.getOutputStream();
        if (!isWriteHeader) {
            mOut.write((String.format("HTTP/1.1 %s\r\n", mStatus.toString())).getBytes());
            mOut.write(getHeader().getBytes());
            mOut.write("\r\n".getBytes());
            isWriteHeader = true;
        }
    }


    /**
     * 添加头部信息
     *
     * @param key
     * @param value
     */
    public void addHeader(String key, String value) {
        mHeader.put(key, value);
    }

    // 获取头部信息
    private String getHeader() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : mHeader.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        return sb.toString();
    }


    // 静默关闭可关闭的对象
    private static void quietClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
