package tuuzed.lib.microhttpd;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface HttpRequestDispatcher {
    @Nullable
    HttpResponse dispatch(@NotNull HttpRequest req);
}
