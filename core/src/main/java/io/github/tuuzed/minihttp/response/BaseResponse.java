package io.github.tuuzed.minihttp.response;

import java.util.HashMap;
import java.util.Map;

public class BaseResponse implements Response {

    protected Status status;
    protected Map<String, String> header;
    protected byte[] body;

    protected BaseResponse() {
        header = new HashMap<>();
        addHeader("Server", "MiniHTTPd");
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public void addHeader(String key, String value) {
        header.put(key, value);
    }

    public void removeHeader(String key) {
        header.remove(key);
    }

    public String getHeader() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        return sb.toString();
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public byte[] getBody() {
        return body;
    }
}
