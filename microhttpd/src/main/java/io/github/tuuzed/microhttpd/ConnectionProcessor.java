package io.github.tuuzed.microhttpd;

import io.github.tuuzed.microhttpd.handler.Handler;
import io.github.tuuzed.microhttpd.request.Request;
import io.github.tuuzed.microhttpd.request.RequestImpl;
import io.github.tuuzed.microhttpd.response.Response;
import io.github.tuuzed.microhttpd.response.ResponseImpl;
import io.github.tuuzed.microhttpd.response.Status;
import io.github.tuuzed.microhttpd.util.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

class ConnectionProcessor implements Runnable {
    private static final Logger sLogger = Logger.getLogger(MicroHTTPdImpl.class);
    private Socket mConnect;
    private RequestsDispatcher mDispatcher;

    ConnectionProcessor(RequestsDispatcher dispatcher, Socket connect) {
        mDispatcher = dispatcher;
        mConnect = connect;
    }

    @Override
    public void run() {
        Response response = new ResponseImpl(mConnect);
        InputStream in = null;
        try {
            in = mConnect.getInputStream();
            Request request = RequestImpl.getRequest(in);
            if (request != null) {
                // 符合协议的请求
                Handler handler = mDispatcher.getHandler(request.getUrl());
                if (handler != null) {
                    handler.serve(request, response);
                } else {
                    response.renderError(Status.STATUS_404);
                }
            } else {
                // 不是HTTP请求
                response.close();
            }
        } catch (IOException e) {
            sLogger.e(e);
        } finally {
            safeClose(in);
            safeClose(response);
            sLogger.d(String.format("Client (%d) disconnect...", mConnect.hashCode()));
        }
    }

    private void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                sLogger.e(e);
            }
        }
    }

}
