package com.tuuzed.microhttpd.http;

public enum Status {
    // 正常
    STATUS_200(200, "OK"),
    // 重定向
    STATUS_301(301, "Moved Permanently"),
    // 找到
    STATUS_302(302, "Found"),
    // 临时重定向
    STATUS_307(307, "Temporary Redirect"),
    // 错误请求
    STATUS_400(400, "Bad RequestImpl"),
    // 未授权
    STATUS_401(401, "Unauthorized"),
    // 禁止
    STATUS_403(403, "Forbidden"),
    // 未找到
    STATUS_404(404, "Not Found"),
    // 方法未允许
    STATUS_405(405, "Method Not Allowed"),
    // 已经不存在
    STATUS_410(410, "Gone"),
    // 内部服务器错误
    STATUS_500(500, "Internal Server Error"),
    // 未实现
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
