package com.tuuzed.microhttpd.common.log;

import org.jetbrains.annotations.NotNull;

public class LoggerFactory {
    private volatile static boolean globalDebug;
    private volatile static boolean globalPrintStackTrace;

    public static void setGlobalDebug(boolean debug) {
        LoggerFactory.globalDebug = debug;
    }

    public static void setGlobalPrintStackTrace(boolean printStackTrace) {
        LoggerFactory.globalPrintStackTrace = printStackTrace;
    }


    public static Logger getLogger(@NotNull String name) {
        return new SimpleLogger.Builder()
                .setDebug(globalDebug)
                .setPrintStackTrace(globalPrintStackTrace)
                .setName(name)
                .build();
    }

    public static Logger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }

}
