package tuuzed.lib.microhttpd.core;

import org.jetbrains.annotations.NotNull;
import tuuzed.lib.microhttpd.StatusCode;

public enum StatusCodeEnum implements StatusCode {

    STATUS_200(200, "OK"),
    STATUS_301(301, "Moved Permanently"),
    STATUS_302(302, "Found"),
    STATUS_307(307, "Temporary Redirect"),
    STATUS_400(400, "Bad RealHttpRequest"),
    STATUS_401(401, "Unauthorized"),
    STATUS_403(403, "Forbidden"),
    STATUS_404(404, "Not Found"),
    STATUS_405(405, "Method Not Allowed"),
    STATUS_410(410, "Gone"),
    STATUS_500(500, "Internal Server Error"),
    STATUS_501(501, "501 Not Implemented");

    private int code;
    private String semantics;

    StatusCodeEnum(int code, String semantics) {
        this.code = code;
        this.semantics = semantics;
    }

    @Override
    public int code() {
        return code;
    }

    @NotNull
    @Override
    public String semantics() {
        return semantics;
    }

    @Override
    public String toString() {
        return code + " " + semantics; // 200 OK
    }
}
