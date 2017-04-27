package io.github.tuuzed.microhttpd;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import io.github.tuuzed.microhttpd.handler.Handler;
import io.github.tuuzed.microhttpd.request.Request;
import io.github.tuuzed.microhttpd.request.RequestImpl;
import io.github.tuuzed.microhttpd.response.Response;
import io.github.tuuzed.microhttpd.response.ResponseImpl;
import io.github.tuuzed.microhttpd.response.Status;
import io.github.tuuzed.microhttpd.util.Logger;

class ClientProcessRunnable implements Runnable {
    private static final Logger sLogger = Logger.getLogger(MicroHTTPdImpl.class);
    private int mBufSize;
    private Socket mClient;
    private RequestsDispatcher mDispatcher;

    ClientProcessRunnable(RequestsDispatcher dispatcher, Socket client, int bufSize) {
        mDispatcher = dispatcher;
        mClient = client;
        mBufSize = bufSize;
    }

    @Override
    public void run() {
        Response response = new ResponseImpl(mClient);
        InputStream in = null;
        // 一个用于缓存输入流的输出流
        ByteArrayOutputStream buffInOut = null;
        try {
            in = mClient.getInputStream();
            buffInOut = new ByteArrayOutputStream();
            byte[] buf = new byte[mBufSize];
            // 读取输入流，堵塞程序
            int read = in.read(buf);
            buffInOut.write(buf, 0, read);
            while (in.available() != 0) {
                read = in.read(buf);
                buffInOut.write(buf, 0, read);
            }
            Request request = RequestImpl.getRequest(buffInOut.toByteArray());
            if (request == null) {
                // 不符合协议的请求
                response.setStatus(Status.STATUS_400);
                response.write(Status.STATUS_400.toString());
            } else {
                // 符合协议的请求
                Handler handler = mDispatcher.getHandler(request.getUrl());
                if (handler != null) {
                    handler.serve(request, response);
                } else {
                    // 请求URL不存在
                    response.setStatus(Status.STATUS_404);
                    response.write(Status.STATUS_404.toString());
                }
            }
        } catch (IOException e) {
            sLogger.e(e);
        } finally {
            safeClose(buffInOut);
            safeClose(in);
            safeClose(response);
            sLogger.d(String.format("Client (%d) disconnect...", mClient.hashCode()));
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
