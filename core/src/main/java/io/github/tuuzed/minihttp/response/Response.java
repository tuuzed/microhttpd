package io.github.tuuzed.minihttp.response;

/**
 * HTTP响应
 */
public interface Response {
    String STATUS_200 = "200 OK";
    String STATUS_301 = "301 Moved Permanently";
    String STATUS_302 = "302 Found";
    String STATUS_307 = "307 Temporary Redirect";
    String STATUS_400 = "400 Bad RequestImpl";
    String STATUS_401 = "401 Unauthorized";
    String STATUS_403 = "403 Forbidden";
    String STATUS_404 = "404 Not Found";
    String STATUS_405 = "405 Method Not Allowed";
    String STATUS_410 = "410 Gone";
    String STATUS_500 = "500 Internal Server Error";
    String STATUS_501 = "501 Not Implemented";

    String getStatus();

    String getHeader();

    byte[] getBody();

}
