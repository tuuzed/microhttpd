package io.github.tuuzed.microhttpd.request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import io.github.tuuzed.microhttpd.util.Logger;
import io.github.tuuzed.microhttpd.util.TextUtils;

/**
 * HTTP请求
 */
public class RequestImpl implements Request {
    private static final Logger sLogger = Logger.getLogger(RequestImpl.class);

    private String method;
    private String uri;
    private String protocol;
    private Map<String, String> header;
    private Map<String, String> params;
    private Map<String, String> data;

    public static Request getRequest(byte[] bytes) {
        String encoding = "utf-8";
        String rawRequest = new String(bytes).trim();
        sLogger.d("rawRequest:\n" + rawRequest);
        String[] rawArray = rawRequest.split("\r\n");
        boolean isEndHeader = false;
        Map<String, String> header = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        Map<String, String> data = new HashMap<>();
        String method = null;
        String uri = null;
        String protocol = null;
        StringBuilder body = new StringBuilder();
        for (int i = 0; i < rawArray.length; i++) {
            String s = rawArray[i];
            if (i == 0) {
                // 请求方法 URI 协议
                // GET / HTTP/1.1
                String[] split = s.split(" ");
                // 如果数组长度小于3则不符合协议
                if (split.length < 3) {
                    return null;
                }
                method = split[0];
                uri = split[1];
                protocol = split[2];
                // 存在空字符则不符合协议
                if (TextUtils.isEmpty(method) || TextUtils.isEmpty(uri) || TextUtils.isEmpty(protocol)) {
                    return null;
                }
                method = method.toUpperCase();
                // 提取 params
                if (uri.contains("?")) {
                    String[] split1 = uri.split("\\?");
                    uri = split1[0];
                    for (String str : split1[1].split("&")) {
                        String[] split2 = str.split("=");
                        try {
                            params.put(URLDecoder.decode(split2[0], encoding),
                                    URLDecoder.decode(split2[1], encoding));
                        } catch (UnsupportedEncodingException e) {
                            sLogger.e(e);
                        }
                    }
                }
                try {
                    uri = URLDecoder.decode(uri, encoding);
                } catch (UnsupportedEncodingException e) {
                    sLogger.e(e);
                }
            } else {
                // 匹配的空行则说明Header结束
                if ("".equals(s)) {
                    isEndHeader = true;
                } else if (isEndHeader) {
                    // Header部分结束
                    body.append(s.trim());
                } else {
                    // Header部分
                    String[] split = s.split(":");
                    header.put(split[0], split[1]);
                }
            }
        }
        String sBody = body.toString();
        if (!TextUtils.isEmpty(sBody)) {
            for (String s : body.toString().split("&")) {
                String[] split = s.split("=");
                data.put(split[0], split[1]);
            }
        }
        return new RequestImpl(method, uri, protocol, header, params, data);
    }

    private RequestImpl(String method,
                        String uri,
                        String protocol,
                        Map<String, String> header,
                        Map<String, String> params,
                        Map<String, String> data) {
        this.method = method;
        this.uri = uri;
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
    public String getUri() {
        return uri;
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
        return "Request{" +
                "method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", protocol='" + protocol + '\'' +
                ", header=" + header +
                ", params=" + params +
                ", data=" + data +
                '}';
    }
}
