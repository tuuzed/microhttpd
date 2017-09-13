package com.tuuzed.microhttpd;

import com.tuuzed.microhttpd.route.Routes;
import org.jetbrains.annotations.NotNull;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 请求调度器
 */
class RequestDispatcher {
    private ExecutorService mDispatchThreadPool;
    private Routes mRoutes;

    static RequestDispatcher create(int nThreads, @NotNull Routes routes) {
        return new RequestDispatcher(nThreads, routes);
    }

    private RequestDispatcher(int nThreads, Routes routes) {
        // 未指定线程数量则使用可缓存的线程池
        mDispatchThreadPool = nThreads <= 0 ? Executors.newCachedThreadPool() : Executors.newFixedThreadPool(nThreads);
        this.mRoutes = routes;
    }

    /**
     * 调度
     *
     * @param socket 客户端连接Socket对象
     */
    void dispatch(@NotNull Socket socket) {
        mDispatchThreadPool.execute(AcceptHandler.create(mRoutes, socket));
    }
}

