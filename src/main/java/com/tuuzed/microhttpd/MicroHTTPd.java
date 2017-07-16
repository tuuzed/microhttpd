package com.tuuzed.microhttpd;

import java.io.IOException;

import com.tuuzed.microhttpd.handler.Handler;

public interface MicroHTTPd {

    /**
     * 注册 Handler
     *
     * @param route   :路由(支持正则匹配)
     * @param handler :处理者
     */
    void register(String route, Handler handler);


    /**
     * 启动
     */
    void startup() throws IOException;

    /**
     * 停止
     */
    void stop();
}
