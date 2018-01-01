package tuuzed.lib.microhttpd.internal;

import org.jetbrains.annotations.NotNull;
import tuuzed.lib.microhttpd.MicroHTTPD;

import java.io.UnsupportedEncodingException;

public final class URLEncoder {
    public static String encode(@NotNull String text) {
        try {
            return java.net.URLEncoder.encode(text, MicroHTTPD.sDefaultCharset.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }
}
