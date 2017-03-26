package io.github.tuuzed.microhttpd;


import io.github.tuuzed.microhttpd.exception.URIRegexException;
import io.github.tuuzed.microhttpd.handler.Handler;
import io.github.tuuzed.microhttpd.handler.StaticFileHandler;
import io.github.tuuzed.microhttpd.util.Logger;
import io.github.tuuzed.microhttpd.util.TextUtils;

import java.io.File;

class MicroHTTPdImpl implements MicroHTTPd {
    private static final Logger sLogger = Logger.getLogger(MicroHTTPdImpl.class);
    private RequestsDispatcher mDispatcher;
    private String address;
    private int port;
    private int timeout;

    MicroHTTPdImpl(MicroHTTPdBuilder builder) {
        Logger.setDebug(builder.debug);
        this.address = builder.address;
        this.port = builder.port;
        this.timeout = builder.timeout;
        mDispatcher = new RequestsDispatcher(builder.threadNumber, builder.buffSize);
        if (!TextUtils.isEmpty(builder.staticPath)) {
            File file = new File(builder.staticPath);
            if (file.exists() && file.isDirectory()) {
                if (TextUtils.isEmpty(builder.staticUriRegex)) {
                    builder.staticUriRegex = "^/static/.*";
                }
                register(builder.staticUriRegex, new StaticFileHandler(builder.staticUriRegex, file));
            }
        }
    }

    @Override
    public void listen() {
        new Thread(new ServerListenRunnable(mDispatcher, address, port, timeout)).start();
        sLogger.d(String.format("Server is running at : http://%s:%d", address, port));
    }

    @Override
    public void register(String regex, Handler handler) {
        if (regex.length() > 2 && "^/".equals(regex.substring(0, 2))) {
            mDispatcher.register(regex, handler);
        } else {
            throw new URIRegexException(regex);
        }
    }

}

