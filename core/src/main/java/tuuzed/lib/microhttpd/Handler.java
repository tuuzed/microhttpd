package tuuzed.lib.microhttpd;

import org.jetbrains.annotations.NotNull;

public interface Handler {
    @NotNull
    HttpResponse handle(@NotNull HttpRequest req);
}