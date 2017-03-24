package io.github.tuuzed.minihttp;

import io.github.tuuzed.minihttp.request.Request;
import io.github.tuuzed.minihttp.request.RequestImpl;
import io.github.tuuzed.minihttp.response.Response;
import io.github.tuuzed.minihttp.response.ResponseImpl;
import io.github.tuuzed.minihttp.response.Status;
import io.github.tuuzed.minihttp.util.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

class SocketRunnable implements Runnable {
    private static final Logger sLogger = Logger.getLogger(MiniHTTPd.class);
    private Socket client;
    private int buffSize;
    private RequestsDispatcher mDispatcher;

    SocketRunnable(RequestsDispatcher dispatcher, Socket client, int buffSize) {
        this.mDispatcher = dispatcher;
        this.client = client;
        this.buffSize = buffSize;
    }

    @Override
    public void run() {
        Response response = new ResponseImpl(client);
        InputStream in = null;
        try {
            in = client.getInputStream();
            byte[] bytes = new byte[buffSize];
            int read = in.read(bytes);
            String rawRequest = new String(bytes).trim();
            sLogger.d("rawRequest:\n" + rawRequest);
            Request request = RequestImpl.analysis(rawRequest);
            if (request == null) {
                // 不符合协议的请求
                response.setStatus(Status.STATUS_400);
                response.write(Status.STATUS_400.toString());
            } else {
                // 符合协议的请求
                Handler handler = mDispatcher.getHandler(request.getUri());
                if (handler != null) {
                    handler.serve(request, response);
                } else {
                    // 请求URI不存在
                    response.setStatus(Status.STATUS_404);
                    response.write(Status.STATUS_404.toString());
                }
            }
        } catch (IOException e) {
            sLogger.e(e);
        } finally {
            quietClose(in);
            quietClose(response);
            sLogger.d(String.format("客户端(%d)断开...", hashCode()));
        }
    }

    // 静默关闭可关闭的对象
    private static void quietClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
