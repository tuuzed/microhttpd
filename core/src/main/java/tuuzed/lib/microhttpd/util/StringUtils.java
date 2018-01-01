package tuuzed.lib.microhttpd.util;

public class StringUtils {
    public static boolean isEmpty(String text) {
        return text == null || text.trim().length() == 0;
    }

    public static String trim(String text) {
        if (text == null) return "";
        return text.trim();
    }
}
