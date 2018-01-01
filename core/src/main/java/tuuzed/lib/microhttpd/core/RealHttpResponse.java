package tuuzed.lib.microhttpd.core;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tuuzed.lib.microhttpd.Headers;
import tuuzed.lib.microhttpd.HttpResponse;
import tuuzed.lib.microhttpd.ResponseBody;
import tuuzed.lib.microhttpd.ResponseLine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

final class RealHttpResponse implements HttpResponse {
    private static final String CRLF = "\r\n";
    private RealResponseLine responseLine;
    private Headers headers;
    @Nullable
    private ResponseBody responseBody;

    RealHttpResponse(@NotNull RealResponseLine responseLine, @NotNull Headers headers,
                     @Nullable ResponseBody responseBody) {
        this.responseLine = responseLine;
        this.headers = headers;
        this.responseBody = responseBody;
    }

    @Override
    public void respond(@NotNull OutputStream outputStream) throws IOException {
        DataOutputStream out = new DataOutputStream(outputStream);
        // responseLine
        out.writeBytes(responseLineToString(responseLine));
        out.writeBytes(CRLF);
        // headers
        out.writeBytes(headersToString(headers));
        // 空行
        out.writeBytes(CRLF);
        // responseBody
        if (responseBody != null) {
            responseBody.writeTo(outputStream);
        }
        outputStream.flush();
    }

    private String responseLineToString(ResponseLine responseLine) {
        return responseLine.protocol().protocolName() + "/" + responseLine.protocol().version()
                + " " + responseLine.sc().code() + " " + responseLine.sc().semantics();
    }

    private static String headersToString(Headers headers) {
        StringBuilder result = new StringBuilder();
        for (int i = 0, size = headers.size(); i < size; i++) {
            // Content-Type: text/plain; charset=utf-8
            result.append(headers.keyAt(i))
                    .append(": ")
                    .append(headers.valueAt(i))
                    .append(CRLF);
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return responseLine + "\n"
                + headers + "\n"
                ;
    }

    @Override
    public ResponseLine responseLine() {
        return responseLine;
    }

    @Override
    public Headers headers() {
        return headers;
    }

    @Override
    public ResponseBody body() {
        return null;
    }
}
