package tuuzed.lib.microhttpd;

public interface HttpRequest {

    RequestLine requestLine();

    Headers headers();

    RequestBody body();

}
