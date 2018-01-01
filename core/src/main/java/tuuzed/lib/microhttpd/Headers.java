package tuuzed.lib.microhttpd;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface Headers {
    @Nullable
    String get(@NotNull String name);

    String put(@NotNull String key, @NotNull String value);

    @Nullable
    String remove(@NotNull String key);

    @NotNull
    String[] keyArray();

    @NotNull
    String[] valueArray();

    int size();

    boolean isEmpty();

    void putAllOf(@NotNull Map<String, String> map);

    String keyAt(int index);

    String valueAt(int index);
}
