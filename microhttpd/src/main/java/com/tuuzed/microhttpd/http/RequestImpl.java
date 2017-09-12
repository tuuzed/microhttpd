package com.tuuzed.microhttpd.http;

import com.tuuzed.microhttpd.common.util.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestImpl implements Request {
    private static final Logger logger = Logger.getLogger(RequestImpl.class);
    private String mUrl;
    private String mMethod;
    private String mProtocol;
    private Map<String, String> mParams = new HashMap<>();
    private byte[] mData;
    private Map<String, String> mHeaders = new HashMap<>();
    private InputStream mInput;

    @NotNull
    public static Request create(@NotNull InputStream inputStream) throws Exception {
        RequestImpl httpRequest = new RequestImpl(inputStream);
        httpRequest.parser();
        return httpRequest;
    }

    private RequestImpl(@NotNull InputStream inputStream) {
        mInput = inputStream;
    }

    @Override
    public String getMethod() {
        return mMethod;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public String getProtocol() {
        return mProtocol;
    }

    @Override
    public String getHeader(String key) {
        return mHeaders.get(key);
    }

    @Override
    public String getParam(String key) {
        return mParams.get(key);
    }

    @Override
    public byte[] getData() throws IOException {
        if (mData == null && mInput.available() > 0) {
            int len;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            while (mInput.available() > 0) {
                len = mInput.read(buf);
                output.write(buf, 0, len);
            }
            output.flush();
            mData = output.toByteArray();
            output.close();
        }
        return mData;
    }

    private void parser() throws Exception {
        int timer = 0;
        while (mInput.available() <= 0) {
            timer += 10;
            if (timer >= 30_000) throw new SocketTimeoutException("timeout");
            else TimeUnit.MILLISECONDS.sleep(10);
        }
        String line;
        boolean first = true;
        while (true) {
            line = readLine(mInput);
            logger.debug("request line: {}", line);
            if (line == null || line.trim().isEmpty()) break;
            if (first) {
                String[] requestLine = line.split(" ");
                if (requestLine.length != 3) throw new Exception("error");
                // Method
                mMethod = requestLine[0].toUpperCase();
                // Url And Params
                int index = requestLine[1].indexOf('?');
                if (index != -1) {
                    mUrl = URLDecoder.decode(requestLine[1].substring(0, index), "utf-8");
                    String[] params = requestLine[1].substring(index + 1).split("&");
                    for (String param : params) {
                        index = param.indexOf('=');
                        if (index != -1) {
                            String key = URLDecoder.decode(param.substring(0, index), "utf-8");
                            String value = URLDecoder.decode(param.substring(index + 1), "utf-8");
                            mParams.put(key, value);
                        }
                    }
                } else {
                    mUrl = URLDecoder.decode(requestLine[1], "utf-8");
                }
                // Protocol
                mProtocol = requestLine[2].toUpperCase();
            } else {
                int index = line.indexOf(':');
                if (index != -1) {
                    mHeaders.put(line.substring(0, index), line.substring(index + 1).trim());
                }
            }
            first = false;
        }
    }

    private static String readLine(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        int ch;
        for (; ; ) {
            ch = inputStream.read();
            if (ch == -1) break;
            if (ch == '\n') break;
            sb.append((char) ch);
        }
        String line = sb.toString();
        return line.isEmpty() ? null : line;
    }
}
