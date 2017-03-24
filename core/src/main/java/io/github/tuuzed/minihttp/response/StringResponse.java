package io.github.tuuzed.minihttp.response;

/**
 * HTTP响应
 */
public class StringResponse extends BaseResponse {
    private String body;

    public StringResponse(String body) {
        this.status = STATUS_200;
        this.body = body;
    }

    @Override
    public byte[] getBody() {
        return body.getBytes();
    }
}
