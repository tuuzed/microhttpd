package io.github.tuuzed.minihttp;

import io.github.tuuzed.minihttp.handler.Handler;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求调度器
 */
class RequestsDispatcher {
    private Map<Pattern, Handler> mHttpServletMap;
    private ExecutorService mThreadPool;
    private int buffSize;

    RequestsDispatcher(int threadNumber, int buffSize) {
        this.buffSize = buffSize;
        mHttpServletMap = new HashMap<>();
        mThreadPool = Executors.newFixedThreadPool(threadNumber);
    }

    void register(String regex, Handler handler) {
        mHttpServletMap.put(Pattern.compile(regex), handler);
    }

    void dispatch(Socket socket) {
        mThreadPool.execute(new SocketRunnable(this, socket, buffSize));
    }

    Handler getHandler(String uri) {
        for (Map.Entry<Pattern, Handler> entry : mHttpServletMap.entrySet()) {
            Matcher matcher = entry.getKey().matcher(uri);
            if (matcher.find()) {
                return entry.getValue();
            }
        }
        return null;
    }
}

