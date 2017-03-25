package io.github.tuuzed.microhttpd.util;

public class Logger {
    private static boolean debug;
    private String clazzName;

    public static void setDebug(boolean debug) {
        Logger.debug = debug;
    }

    public static boolean isDebug() {
        return debug;
    }

    public Logger(String clazzName) {
        this.clazzName = clazzName;
    }

    public static Logger getLogger(Class clazz) {
        return new Logger(clazz.getName());
    }

    public void e(String msg) {
        if (debug) {
            System.err.printf("[ERROR:]%s => %s%n", clazzName, msg);
        }
    }

    public void e(Throwable throwable) {
        if (debug) {
            throwable.printStackTrace();
            System.err.printf("[ERROR:] %s => %s%n", clazzName, throwable.toString());
        }
    }

    public void d(String msg) {
        if (debug) {
            System.out.printf("[DEBUG:] %s => %s%n", clazzName, msg);
        }
    }

    public void d(Throwable throwable) {
        if (debug) {
            throwable.printStackTrace();
            System.out.printf("[DEBUG:] %s => %s%n", clazzName, throwable.toString());
        }
    }
}
