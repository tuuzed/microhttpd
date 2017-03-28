package io.github.tuuzed.microhttpd;

public class MicroHTTPdBuilder {
    int port;
    int threadNumber;
    int buffSize;
    int timeout;
    boolean debug;

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

    public MicroHTTPd build() {
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
