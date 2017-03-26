package io.github.tuuzed.microhttpd;

import io.github.tuuzed.microhttpd.handler.Handler;
import io.github.tuuzed.microhttpd.request.Request;
import io.github.tuuzed.microhttpd.request.RequestImpl;
import io.github.tuuzed.microhttpd.response.Response;
import io.github.tuuzed.microhttpd.response.ResponseImpl;
import io.github.tuuzed.microhttpd.response.Status;
import io.github.tuuzed.microhttpd.util.Logger;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

class HandleClientRunnable implements Runnable {
    private static final Logger sLogger = Logger.getLogger(MicroHTTPdImpl.class);
    private Socket client;
    private int buffSize;
    private RequestsDispatcher mDispatcher;

    HandleClientRunnable(RequestsDispatcher dispatcher, Socket client, int buffSize) {
        this.mDispatcher = dispatcher;
        this.client = client;
        this.buffSize = buffSize;
    }

    @Override
    public void run() {
        Response response = new ResponseImpl(client);
        InputStream in = null;
        // 一个用于缓存输入流的输出流
        ByteArrayOutputStream buffInOut = null;
        try {
            in = client.getInputStream();
            buffInOut = new ByteArrayOutputStream();
            byte[] buf = new byte[buffSize];
            int read = in.read(buf);
            buffInOut.write(buf);
            while (in.available() != 0) {
                Arrays.fill(buf, (byte) 0);
                read = in.read(buf);
                buffInOut.write(buf);
            }
            Request request = RequestImpl.getRequest(buffInOut.toByteArray());
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
            quietClose(buffInOut);
            quietClose(in);
            quietClose(response);
            sLogger.d(String.format("Client (%d) disconnect...", client.hashCode()));
        }
    }

    // 静默关闭可关闭的对象
    private static void quietClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                sLogger.e(e);
            }
        }
    }
}
