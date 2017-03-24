package io.github.tuuzed.minihttp.response;

public enum Status {
    STATUS_200(200, "OK"),
    STATUS_301(301, "Moved Permanently"),
    STATUS_302(302, "Found"),
    STATUS_307(307, "Temporary Redirect"),
    STATUS_400(400, "Bad RequestImpl"),
    STATUS_401(401, "Unauthorized"),
    STATUS_403(401, "Forbidden"),
    STATUS_404(404, "Not Found"),
    STATUS_405(405, "Method Not Allowed"),
    STATUS_410(410, "Gone"),
    STATUS_500(500, "Internal Server Error"),
    STATUS_501(501, "501 Not Implemented");

    private int code;
    private String semantics;

    Status(int code, String semantics) {
        this.code = code;
        this.semantics = semantics;
    }

    public int getCode() {
        return code;
    }

    public String getSemantics() {
        return semantics;
    }

    @Override
    public String toString() {
        return String.format("%d %s", code, semantics);
    }
}
