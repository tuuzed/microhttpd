package tuuzed.lib.microhttpd;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

public interface HttpResponse {

    ResponseLine responseLine();

    Headers headers();

    ResponseBody body();

    void respond(@NotNull OutputStream outputStream) throws IOException;
}
