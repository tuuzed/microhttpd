package io.github.tuuzed.minihttp.response;

/**
 * HTTP响应
 */
public class StringResponse extends BaseResponse {
    private String body;

    public StringResponse(Status status) {
        this(status, status.toString());
    }

    public StringResponse(String body) {
        this(Status.STATUS_200, body);
    }

    public StringResponse(Status status, String body) {
        this.status = status;
        addHeader("Content-Type", "text/plain; charset=utf-8");
        this.body = body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public byte[] getBody() {
        return body.getBytes();
    }
}
