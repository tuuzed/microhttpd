package io.github.tuuzed.microhttpd;

import io.github.tuuzed.microhttpd.handler.Handler;
import io.github.tuuzed.microhttpd.request.Request;
import io.github.tuuzed.microhttpd.request.RequestImpl;
import io.github.tuuzed.microhttpd.response.Response;
import io.github.tuuzed.microhttpd.response.ResponseImpl;
import io.github.tuuzed.microhttpd.response.Status;
import io.github.tuuzed.microhttpd.util.CloseableUtils;
import io.github.tuuzed.microhttpd.util.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

class ClientProcessRunnable implements Runnable {
    private static final Logger sLogger = Logger.getLogger(MicroHTTPdImpl.class);
    private Socket client;
    private static final int BUF_SIZE = 1024;
    private RequestsDispatcher mDispatcher;

    ClientProcessRunnable(RequestsDispatcher dispatcher, Socket client) {
        this.mDispatcher = dispatcher;
        this.client = client;
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
            byte[] buf = new byte[BUF_SIZE];
            int read = in.read(buf);
            for (int i = read; i < BUF_SIZE; i++) {
                buf[i] = (byte) 0;
            }
            buffInOut.write(buf);
            while (in.available() != 0) {
                read = in.read(buf);
                // 缓存区中的数据未被刷新的全部填充为0
                for (int i = read; i < BUF_SIZE; i++) {
                    buf[i] = (byte) 0;
                }
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
            CloseableUtils.quietClose(buffInOut);
            CloseableUtils.quietClose(in);
            CloseableUtils.quietClose(response);
            sLogger.d(String.format("Client (%d) disconnect...", client.hashCode()));
        }
    }

}
