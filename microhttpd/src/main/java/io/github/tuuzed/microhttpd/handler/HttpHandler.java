package io.github.tuuzed.microhttpd.handler;

import io.github.tuuzed.microhttpd.request.Request;
import io.github.tuuzed.microhttpd.response.Response;
import io.github.tuuzed.microhttpd.response.Status;
import io.github.tuuzed.microhttpd.util.Logger;

import java.io.IOException;

/**
 * Http请求处理
 */
public class HttpHandler implements Handler {
    private final static Logger sLogger = Logger.getLogger(StaticFileHandler.class);

    @Override
    public void serve(Request request, Response response) throws IOException {
        sLogger.d("接收到请求..." + request.toString());
        if ("GET".equals(request.getMethod())) {
            doGet(request, response);
        } else if ("POST".equals(request.getMethod())) {
            doPost(request, response);
        } else if ("PUT".equals(request.getMethod())) {
            doPut(request, response);
        } else if ("DELETE".equals(request.getMethod())) {
            doDelete(request, response);
        } else if ("PATCH".equals(request.getMethod())) {
            doPatch(request, response);
        } else if ("HEAD".equals(request.getMethod())) {
            doHead(request, response);
        } else if ("CONNECT".equals(request.getMethod())) {
            doConnect(request, response);
        } else if ("OPTIONS".equals(request.getMethod())) {
            doOptions(request, response);
        } else if ("TRACE".equals(request.getMethod())) {
            doTrace(request, response);
        } else {
            response405(response);
        }
    }

    /**
     * GET 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doGet(Request request, Response response) throws IOException {
        response405(response);
    }

    /**
     * POST 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doPost(Request request, Response response) throws IOException {
        response405(response);
    }

    /**
     * PUT 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doPut(Request request, Response response) throws IOException {
        response405(response);
    }

    /**
     * DELETE 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doDelete(Request request, Response response) throws IOException {
        response405(response);
    }

    /**
     * PATCH 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doPatch(Request request, Response response) throws IOException {
        response405(response);
    }

    /**
     * HEAD 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doHead(Request request, Response response) throws IOException {
        response405(response);
    }

    /**
     * CONNECT 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doConnect(Request request, Response response) throws IOException {
        response405(response);
    }

    /**
     * OPTIONS 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doOptions(Request request, Response response) throws IOException {
        response405(response);
    }

    /**
     * TRACE 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doTrace(Request request, Response response) throws IOException {
        response405(response);
    }

    // 响应405，方法未允许
    private void response405(Response response) throws IOException {
        response.setStatus(Status.STATUS_405);
        response.write(Status.STATUS_405.toString());
    }
}
