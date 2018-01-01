package tuuzed.lib.microhttpd.core;

import tuuzed.lib.microhttpd.*;
import tuuzed.lib.microhttpd.internal.MimeType;
import tuuzed.lib.microhttpd.internal.URLEncoder;

import java.io.File;
import java.util.Date;

public final class HttpResponses {

    public static HttpResponse html(String html) {
        byte[] body = html.getBytes(MicroHTTPD.sDefaultCharset);
        return bytes(StatusCodeEnum.STATUS_200,
                "text/html; charset=" + MicroHTTPD.sDefaultCharset.name(),
                body, 0, body.length);
    }

    public static HttpResponse text(String text) {
        byte[] body = text.getBytes(MicroHTTPD.sDefaultCharset);
        return bytes(StatusCodeEnum.STATUS_200,
                "text/plain; charset=" + MicroHTTPD.sDefaultCharset.name(),
                body, 0, body.length);
    }

    public static HttpResponse json(String json) {
        byte[] body = json.getBytes(MicroHTTPD.sDefaultCharset);
        return bytes(StatusCodeEnum.STATUS_200,
                "application/json; charset=" + MicroHTTPD.sDefaultCharset.name(),
                body, 0, body.length);
    }

    public static HttpResponse xml(String xml) {
        byte[] body = xml.getBytes(MicroHTTPD.sDefaultCharset);
        return bytes(StatusCodeEnum.STATUS_200,
                "text/xml; charset=" + MicroHTTPD.sDefaultCharset.name(),
                body, 0, body.length);
    }

    public static HttpResponse bytes(byte[] bytes) {
        return bytes(bytes, 0, bytes.length);
    }

    public static HttpResponse bytes(byte[] bytes, int offset, int length) {
        return bytes(StatusCodeEnum.STATUS_200, "application/octet-stream", bytes, offset, length);
    }

    public static HttpResponse file(File file) {
        if (!file.exists()) {
            return error(StatusCodeEnum.STATUS_404, "Not Found (" + file.getAbsolutePath() + ")");
        }
        RealResponseLine responseLine = new RealResponseLine(ProtocolEnum.HTTP_1_0, StatusCodeEnum.STATUS_200);
        ResponseBody responseBody = new RealResponseBody(file);
        Headers headers = new RealHeaders(5);
        headers.put("Server", "MicroHTTPD/2.0");
        headers.put("Content-Type", MimeType.getInstance().get(file));
        headers.put("Content-Length", file.length() + "");
        headers.put("Content-Disposition", "inline; filename=" + URLEncoder.encode(file.getName()));
        headers.put("Date", new Date().toString());
        return new RealHttpResponse(responseLine, headers, responseBody);
    }

    public static HttpResponse error(StatusCode sc) {
        return error(sc, sc.semantics());
    }

    public static HttpResponse error(StatusCode sc, String error) {
        final byte[] bodyData = error.getBytes(MicroHTTPD.sDefaultCharset);
        return bytes(sc,
                "text/plain; charset=" + MicroHTTPD.sDefaultCharset.name(),
                bodyData, 0, bodyData.length);
    }

    public static HttpResponse redirect_301(String location) {
        return redirect(location, StatusCodeEnum.STATUS_301);
    }

    public static HttpResponse redirect_302(String location) {
        return redirect(location, StatusCodeEnum.STATUS_302);
    }

    public static HttpResponse redirect_307(String location) {
        return redirect(location, StatusCodeEnum.STATUS_307);
    }

    public static HttpResponse redirect(String location, StatusCode sc) {
        RealResponseLine responseLine = new RealResponseLine(ProtocolEnum.HTTP_1_0, sc);
        Headers headers = new RealHeaders(3);
        headers.put("Server", "MicroHTTPD/2.0");
        headers.put("Date", new Date().toString());
        headers.put("Location", location);
        return new RealHttpResponse(responseLine, headers, null);
    }

    private static HttpResponse bytes(StatusCode sc, String contentType, byte[] bodyData, int offset, int length) {
        RealResponseLine responseLine = new RealResponseLine(ProtocolEnum.HTTP_1_0, sc);
        Headers headers = new RealHeaders(4);
        headers.put("Server", "MicroHTTPD/2.0");
        headers.put("Content-Type", contentType);
        headers.put("Content-Length", length + "");
        headers.put("Date", new Date().toString());
        ResponseBody responseBody = new RealResponseBody(bodyData, offset, length);
        return new RealHttpResponse(responseLine, headers, responseBody);
    }

}
