package tuuzed.lib.microhttpd;

import org.jetbrains.annotations.NotNull;

public interface ResponseLine {
    void setSC(@NotNull StatusCode sc);

    Protocol protocol();

    StatusCode sc();
}
