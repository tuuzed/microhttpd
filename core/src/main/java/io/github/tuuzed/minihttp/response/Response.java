package io.github.tuuzed.minihttp.response;

/**
 * HTTP响应
 */
public interface Response {
    Status getStatus();

    String getHeader();

    byte[] getBody();
}
