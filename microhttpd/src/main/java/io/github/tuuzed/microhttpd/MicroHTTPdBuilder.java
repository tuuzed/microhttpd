package io.github.tuuzed.microhttpd;

import io.github.tuuzed.microhttpd.util.TextUtils;

public class MicroHTTPdBuilder {
    String address;
    int port;
    int threadNumber;
    int buffSize;
    int timeout;
    String staticPath;
    String staticUriRegex;
    boolean debug;

    public MicroHTTPdBuilder setBindAddress(String address) {
        this.address = address;
        return this;
    }

    public MicroHTTPdBuilder setBindPort(int port) {
        this.port = port;
        return this;
    }

    public MicroHTTPdBuilder setFixedThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
        return this;
    }

    public MicroHTTPdBuilder setBuffSize(int buffSize) {
        this.buffSize = buffSize;
        return this;
    }

    public MicroHTTPdBuilder setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public MicroHTTPdBuilder setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public MicroHTTPdBuilder setStaticPath(String staticPath) {
        this.staticPath = staticPath;
        return this;
    }

    public MicroHTTPdBuilder setStaticUriRegex(String staticUriRegex) {
        this.staticUriRegex = staticUriRegex;
        return this;
    }

    public MicroHTTPd build() {
        // 默认绑定地址为127.0.0.1
        if (TextUtils.isEmpty(this.address)) {
            this.address = "127.0.0.1";
        }
        // 默认绑定端口号为5000
        if (this.port == 0) {
            this.port = 5000;
        }
        // 默认缓存大小为1kb
        if (this.buffSize == 0) {
            buffSize = 1024;
        }
        // 默认超时为3秒
        if (this.timeout == 0) {
            this.timeout = 1000 * 3;
        }
        return new MicroHTTPdImpl(this);
    }

}
