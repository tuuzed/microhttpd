package tuuzed.lib.microhttpd;

import org.jetbrains.annotations.NotNull;

public interface StatusCode {
    int code();

    @NotNull
    String semantics();
}
