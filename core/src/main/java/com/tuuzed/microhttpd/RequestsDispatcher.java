package com.tuuzed.microhttpd;

import com.tuuzed.microhttpd.handler.Handler;
import com.tuuzed.microhttpd.util.Logger;

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
    private static final Logger logger = Logger.getLogger(RequestsDispatcher.class);

    private Map<Pattern, Handler> mHandlerMap;
    private ExecutorService mThreadPool;


    RequestsDispatcher(int threadNumber) {
        mHandlerMap = new HashMap<>();
        // 未指定线程数量则使用可缓存的线程池
        if (threadNumber == 0) {
            mThreadPool = Executors.newCachedThreadPool();
        } else {
            mThreadPool = Executors.newFixedThreadPool(threadNumber);
        }
    }

    /**
     * 注册Handler
     *
     * @param route:路由
     * @param handler:处理者
     */
    void register(String route, Handler handler) {
        mHandlerMap.put(Pattern.compile(route), handler);
    }

    /**
     * 调度
     *
     * @param connect:客户端连接Socket对象
     */
    void dispatch(Socket connect) {
        mThreadPool.execute(new ClientConnHandleRunnable(this, connect));
    }

    /**
     * 获取处理者
     *
     * @param url:URL
     * @return :如果获取不到相应的处理器则返回null
     */
    Handler getHandler(String url) {
        for (Map.Entry<Pattern, Handler> entry : mHandlerMap.entrySet()) {
            Matcher matcher = entry.getKey().matcher(url);
            if (matcher.find()) {
                return entry.getValue();
            }
        }
        return null;
    }
}

