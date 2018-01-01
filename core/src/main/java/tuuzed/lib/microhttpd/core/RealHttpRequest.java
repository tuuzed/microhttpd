package tuuzed.lib.microhttpd.core;

import org.jetbrains.annotations.NotNull;
import tuuzed.lib.microhttpd.Headers;
import tuuzed.lib.microhttpd.HttpRequest;
import tuuzed.lib.microhttpd.RequestBody;
import tuuzed.lib.microhttpd.RequestLine;

import java.io.InputStream;

final class RealHttpRequest implements HttpRequest {
    private RequestLine requestLine;
    private Headers headers;
    private RequestBody body;


    RealHttpRequest(@NotNull RequestLine requestLine, @NotNull Headers headers, @NotNull InputStream inputStream) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = new RealRequestBody(inputStream);
    }

    @Override
    public RequestLine requestLine() {
        return requestLine;
    }

    @Override
    public Headers headers() {
        return headers;
    }

    @Override
    public RequestBody body() {
        return body;
    }

    @Override
    public String toString() {
        return requestLine + "\n"
                + headers + "\n"
                ;
    }
}
