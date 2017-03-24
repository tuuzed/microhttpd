package io.github.tuuzed.minihttp;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 请求调度器
 */
class RequestsDispatcher {
    private Map<String, Handler> mHttpServletMap;
    private ExecutorService mThreadPool;
    private int buffSize;

    RequestsDispatcher(int threadNumber,
                       int buffSize) {
        this.buffSize = buffSize;
        mHttpServletMap = new HashMap<>();
        mThreadPool = Executors.newFixedThreadPool(threadNumber);
    }

    void register(String uri, Handler handler) {
        mHttpServletMap.put(uri, handler);
    }

    void dispatch(Socket socket) {
        mThreadPool.execute(new SocketRunnable(this, socket, buffSize));
    }

    Handler getHandler(String uri) {
        return mHttpServletMap.get(uri);
    }
}

