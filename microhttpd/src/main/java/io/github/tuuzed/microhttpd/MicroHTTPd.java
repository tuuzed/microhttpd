package io.github.tuuzed.microhttpd;

import io.github.tuuzed.microhttpd.handler.Handler;

import java.io.IOException;

public interface MicroHTTPd {

    /**
     * 注册 Handler
     *
     * @param regex   :URI 正则表达式
     * @param handler :处理者
     */
    void register(String regex, Handler handler);

    /**
     * 启动
     */
    void startup() throws IOException;

    /**
     * 停止
     */
    void stop();
}
