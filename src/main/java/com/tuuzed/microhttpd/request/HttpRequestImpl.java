package com.tuuzed.microhttpd.request;

import com.tuuzed.microhttpd.util.CloseableUtils;
import com.tuuzed.microhttpd.util.Logger;
import com.tuuzed.microhttpd.util.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求
 */
public class HttpRequestImpl implements HttpRequest {
    private static final Logger logger = Logger.getLogger(HttpRequestImpl.class);
    private static final int BUF_SIZE = 1024;
    private static final String CHARSET = "utf-8";
    private String method;
    private String url;
    private String protocol;
    private Map<String, String> header;
    private Map<String, String> params;
    private Map<String, String> data;

    public static HttpRequest getRequest(InputStream in) {
        ByteArrayOutputStream buffInOut = null;
        HttpRequest httpRequest = null;
        try {
            // 获取全部输入字节
            buffInOut = new ByteArrayOutputStream();
            byte[] buf = new byte[BUF_SIZE];
            // 读取输入流，堵塞程序
            int len = in.read(buf);
            buffInOut.write(buf, 0, len);
            while (in.available() != 0) {
                len = in.read(buf);
                buffInOut.write(buf, 0, len);
            }
            byte[] bytes = buffInOut.toByteArray();
            Map<String, String> header = new HashMap<>();
            String rawRequest = new String(bytes).trim();
            logger.d("raw request:\n" + rawRequest);
            String[] rawArray = rawRequest.split("\r\n");
            // Header部分是否已经结束
            boolean isEndHeader = false;
            StringBuilder body = new StringBuilder();
            if (rawArray.length == 0) return null;
            // 请求行 请求方法 URI 协议
            String requestLine = rawArray[0];

            String[] split = requestLine.split(" ");
            // 如果数组长度小于3则不符合协议
            if (split.length < 3) return null;
            String method = split[0];
            String url = split[1];
            String protocol = split[2];
            // 存在空字符则不符合协议
            if (TextUtils.isEmpty(method) || TextUtils.isEmpty(url) || TextUtils.isEmpty(protocol)) {
                return null;
            }
            method = method.toUpperCase();
            // 提取 params
            Map<String, String> params = new HashMap<>();
            if (url.contains("?")) {
                String[] split1 = url.split("\\?");
                url = split1[0];
                for (String str : split1[1].split("&")) {
                    String[] split2 = str.split("=");
                    try {
                        params.put(URLDecoder.decode(split2[0], CHARSET),
                                URLDecoder.decode(split2[1], CHARSET));
                    } catch (UnsupportedEncodingException e) {
                        logger.e(e);
                    }
                }
            }
            try {
                url = URLDecoder.decode(url, CHARSET);
            } catch (UnsupportedEncodingException e) {
                logger.e(e);
            }
            // 开始解析
            for (int i = 1; i < rawArray.length; i++) {
                String s = rawArray[i];
                if ("".equals(s)) { // 匹配到空行则说明Header结束
                    isEndHeader = true;
                } else if (isEndHeader) {  // Header部分结束,请求正文
                    body.append(s.trim());
                } else {
                    // Header部分
                    split = s.split(":");
                    header.put(split[0], split[1]);
                }
            }
            Map<String, String> data = new HashMap<>();
            String tempBody = body.toString();
            if (!TextUtils.isEmpty(tempBody)) {
                for (String s : tempBody.split("&")) {
                    split = s.split("=");
                    data.put(split[0], split[1]);
                }
            }
            httpRequest = new HttpRequestImpl(method, url, protocol, header, params, data);
        } catch (IOException e) {
            logger.e(e);
        } finally {
            CloseableUtils.safeClose(buffInOut);
        }
        return httpRequest;
    }

    private HttpRequestImpl(String method,
                            String url,
                            String protocol,
                            Map<String, String> header,
                            Map<String, String> params,
                            Map<String, String> data) {
        this.method = method;
        this.url = url;
        this.protocol = protocol;
        this.header = header;
        this.params = params;
        this.data = data;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getHeader(String key) {
        return header.get(key);
    }

    @Override
    public String getParams(String key) {
        return params.get(key);
    }

    @Override
    public String getData(String key) {
        return data.get(key);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", protocol='" + protocol + '\'' +
                ", header=" + header +
                ", params=" + params +
                ", data=" + data +
                '}';
    }
}
