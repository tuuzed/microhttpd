package io.github.tuuzed.microhttpd.request;

import io.github.tuuzed.microhttpd.util.Logger;
import io.github.tuuzed.microhttpd.util.TextUtils;

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
public class RequestImpl implements Request {
    private static final Logger sLogger = Logger.getLogger(RequestImpl.class);

    private String method;
    private String url;
    private String protocol;
    private Map<String, String> header;
    private Map<String, String> params;
    private Map<String, String> data;

    public static Request getRequest(InputStream in) {
        try {
            // 获取全部输入字节
            ByteArrayOutputStream buffInOut = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            // 读取输入流，堵塞程序
            int len = in.read(buf);
            buffInOut.write(buf, 0, len);
            while (in.available() != 0) {
                len = in.read(buf);
                buffInOut.write(buf, 0, len);
            }
            byte[] bytes = buffInOut.toByteArray();
            buffInOut.close();
            String encoding = "utf-8";
            Map<String, String> header = new HashMap<>();
            Map<String, String> params = new HashMap<>();
            Map<String, String> data = new HashMap<>();
            String method = null;
            String url = null;
            String protocol = null;
            String rawRequest = new String(bytes).trim();
            sLogger.d("rawRequest:\n" + rawRequest);
            String[] rawArray = rawRequest.split("\r\n");
            // Header部分是否已经结束
            boolean isEndHeader = false;
            StringBuilder body = new StringBuilder();
            // 开始解析
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
                    url = split[1];
                    protocol = split[2];
                    // 存在空字符则不符合协议
                    if (TextUtils.isEmpty(method) || TextUtils.isEmpty(url) || TextUtils.isEmpty(protocol)) {
                        return null;
                    }
                    method = method.toUpperCase();
                    // 提取 params
                    if (url.contains("?")) {
                        String[] split1 = url.split("\\?");
                        url = split1[0];
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
                        url = URLDecoder.decode(url, encoding);
                    } catch (UnsupportedEncodingException e) {
                        sLogger.e(e);
                    }
                } else {
                    // 匹配到空行则说明Header结束
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
            String tempBody = body.toString();
            if (!TextUtils.isEmpty(tempBody)) {
                for (String s : tempBody.split("&")) {
                    String[] split = s.split("=");
                    data.put(split[0], split[1]);
                }
            }
            return new RequestImpl(method, url, protocol, header, params, data);
        } catch (IOException e) {
            sLogger.e(e);
        }
        return null;
    }

    private RequestImpl(String method,
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
        return "Request{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", protocol='" + protocol + '\'' +
                ", header=" + header +
                ", params=" + params +
                ", data=" + data +
                '}';
    }
}
