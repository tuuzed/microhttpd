package tuuzed.lib.microhttpd;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public interface RequestBody {
    @Nullable
    byte[] bytes() throws IOException;

    boolean file(@NotNull File file) throws IOException;
}
