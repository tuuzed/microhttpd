package tuuzed.lib.microhttpd.internal;

import org.jetbrains.annotations.NotNull;
import tuuzed.lib.microhttpd.MicroHTTPD;

import java.io.UnsupportedEncodingException;

public final class URLDecoder {

    public static String decode(@NotNull String text) {
        try {
            return java.net.URLDecoder.decode(text, MicroHTTPD.sDefaultCharset.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }
}
