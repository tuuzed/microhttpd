package io.github.tuuzed.minihttp.response;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseResponse implements Response {

    protected String status;
    protected Map<String, String> header;
    protected byte[] body;


    protected BaseResponse() {
        header = new HashMap<>();
        header.put("Content-Type", "text/plain; charset=utf-8");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public void putHeader(String key, String value) {
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
