package tuuzed.lib.microhttpd;

import org.jetbrains.annotations.Nullable;

public interface RequestLine {
    String method();

    String url();

    Protocol protocol();

    @Nullable
    QueryParams queryParams();

    String rawRequestLine();
}
