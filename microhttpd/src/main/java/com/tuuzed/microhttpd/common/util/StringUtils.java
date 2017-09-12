package com.tuuzed.microhttpd.common.util;

public class StringUtils {
    /**
     * 判断是否为空字符串
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
