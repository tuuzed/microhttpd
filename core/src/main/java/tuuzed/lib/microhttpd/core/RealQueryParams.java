package tuuzed.lib.microhttpd.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tuuzed.lib.microhttpd.QueryParams;
import tuuzed.lib.microhttpd.internal.ArrayMap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public final class RealQueryParams extends ArrayMap<String, String> implements QueryParams {

    RealQueryParams(int initialCapacity) {
        super(initialCapacity);
    }

    @Nullable
    @Override
    public String get(@NotNull String name) {
        return super.get(name);
    }

    @Override
    public String put(@NotNull String key, @NotNull String value) {
        return super.put(key, value);
    }

    @Nullable
    @Override
    public String remove(@NotNull String key) {
        return super.remove(key);
    }

    @NotNull
    @Override
    public String[] keyArray() {
        Set<String> keySet = keySet();
        String[] names = new String[keySet.size()];
        keySet.toArray(names);
        return names;
    }

    @NotNull
    @Override
    public String[] valueArray() {
        Collection<String> keySet = values();
        String[] names = new String[keySet.size()];
        keySet.toArray(names);
        return names;
    }

    @Override
    public void putAllOf(@NotNull Map<String, String> map) {
        putAll(map);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0, size = size(); i < size; i++) {
            if (i != 0) {
                result.append('&');
            }
            result.append(keyAt(i)).append("=").append(valueAt(i));
        }
        return result.toString();
    }
}
