package io.github.tuuzed.microhttpd;

import io.github.tuuzed.microhttpd.handler.Handler;

public interface MicroHTTPd {

    /**
     * 注册 Handler
     *
     * @param regex   :URI 正则表达式
     * @param handler :处理者
     */
    void register(String regex, Handler handler);

    /**
     * 开始监听
     */
    void listen();

}
