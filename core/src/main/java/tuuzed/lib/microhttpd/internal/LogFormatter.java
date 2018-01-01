package tuuzed.lib.microhttpd.internal;

public class LogFormatter {

    public static String format(String format, Object... arguments) {
        for (Object arg : arguments) {
            format = format.replaceFirst("\\{\\}", String.valueOf(arg));
        }
        return format;
    }
}
