package tuuzed.lib.microhttpd;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

public interface ResponseBody {
    void writeTo(@NotNull OutputStream outputStream) throws IOException;
}
