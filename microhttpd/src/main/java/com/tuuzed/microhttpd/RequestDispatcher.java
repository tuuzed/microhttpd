package com.tuuzed.microhttpd;

import com.tuuzed.microhttpd.view.View;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求调度器
 */
class RequestDispatcher {

    private Map<Pattern, View> mRouteViews;
    private ExecutorService mThreadPool;

    static RequestDispatcher create(int nThreads) {
        return new RequestDispatcher(nThreads);
    }


    private RequestDispatcher(int nThreads) {
        mRouteViews = new LinkedHashMap<>();
        // 未指定线程数量则使用可缓存的线程池
        if (nThreads <= 0) {
            mThreadPool = Executors.newCachedThreadPool();
        } else {
            mThreadPool = Executors.newFixedThreadPool(nThreads);
        }
    }

    /**
     * 注册Handler
     *
     * @param route 路由
     * @param view  处理者
     */
    void register(@NotNull String route, @NotNull View view) {
        mRouteViews.put(Pattern.compile(route), view);
    }

    /**
     * 调度
     *
     * @param socket 客户端连接Socket对象
     */
    void dispatch(@NotNull Socket socket) {
        mThreadPool.execute(new AcceptHandler(this, socket));
    }

    /**
     * 获取处理者
     *
     * @param url URL
     * @return 如果获取不到相应的处理器则返回null
     */
    @Nullable
    View getView(@NotNull String url) {
        for (Map.Entry<Pattern, View> entry : mRouteViews.entrySet()) {
            Matcher matcher = entry.getKey().matcher(url);
            if (matcher.find()) {
                return entry.getValue();
            }
        }
        return null;
    }
}

