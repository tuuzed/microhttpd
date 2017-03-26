package io.github.tuuzed.microhttpd;

import io.github.tuuzed.microhttpd.handler.Handler;

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
    private Map<Pattern, Handler> mHandlerMap;
    private ExecutorService mThreadPool;
    private int buffSize;

    RequestsDispatcher(int threadNumber, int buffSize) {
        this.buffSize = buffSize;
        mHandlerMap = new HashMap<>();
        // 未指定线程数量则使用可缓存的线程池
        if (threadNumber == 0) {
            mThreadPool = Executors.newCachedThreadPool();
        } else {
            mThreadPool = Executors.newFixedThreadPool(threadNumber);
        }
    }

    void register(String regex, Handler handler) {
        mHandlerMap.put(Pattern.compile(regex), handler);
    }

    void dispatch(Socket socket) {
        mThreadPool.execute(new HandleClientRunnable(this, socket, buffSize));
    }

    Handler getHandler(String uri) {
        for (Map.Entry<Pattern, Handler> entry : mHandlerMap.entrySet()) {
            Matcher matcher = entry.getKey().matcher(uri);
            if (matcher.find()) {
                return entry.getValue();
            }
        }
        return null;
    }
}

