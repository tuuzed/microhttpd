package com.tuuzed.microhttpd.handler;

import java.io.IOException;

import com.tuuzed.microhttpd.request.Method;
import com.tuuzed.microhttpd.request.HttpRequest;
import com.tuuzed.microhttpd.response.HttpResponse;
import com.tuuzed.microhttpd.response.Status;
import com.tuuzed.microhttpd.util.Logger;

/**
 * Http请求处理
 */
public class HttpHandler implements Handler {
    private final static Logger logger = Logger.getLogger(HttpHandler.class);

    @Override
    public void serve(HttpRequest request, HttpResponse response) throws IOException {
        logger.d("Receive request..." + request.toString());
        if (Method.GET.equals(request.getMethod())) {
            doGet(request, response);
        } else if (Method.POST.equals(request.getMethod())) {
            doPost(request, response);
        } else if (Method.PUT.equals(request.getMethod())) {
            doPut(request, response);
        } else if (Method.DELETE.equals(request.getMethod())) {
            doDelete(request, response);
        } else if (Method.PATCH.equals(request.getMethod())) {
            doPatch(request, response);
        } else if (Method.HEAD.equals(request.getMethod())) {
            doHead(request, response);
        } else if (Method.CONNECT.equals(request.getMethod())) {
            doConnect(request, response);
        } else if (Method.OPTIONS.equals(request.getMethod())) {
            doOptions(request, response);
        } else if (Method.TRACE.equals(request.getMethod())) {
            doTrace(request, response);
        } else {
            response.renderError(Status.STATUS_405);
        }
    }

    /**
     * GET 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        response.renderError(Status.STATUS_405);

    }

    /**
     * POST 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
        response.renderError(Status.STATUS_405);

    }

    /**
     * PUT 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doPut(HttpRequest request, HttpResponse response) throws IOException {
        response.renderError(Status.STATUS_405);

    }

    /**
     * DELETE 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doDelete(HttpRequest request, HttpResponse response) throws IOException {
        response.renderError(Status.STATUS_405);

    }

    /**
     * PATCH 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doPatch(HttpRequest request, HttpResponse response) throws IOException {
        response.renderError(Status.STATUS_405);

    }

    /**
     * HEAD 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doHead(HttpRequest request, HttpResponse response) throws IOException {
        response.renderError(Status.STATUS_405);

    }

    /**
     * CONNECT 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doConnect(HttpRequest request, HttpResponse response) throws IOException {
        response.renderError(Status.STATUS_405);

    }

    /**
     * OPTIONS 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doOptions(HttpRequest request, HttpResponse response) throws IOException {
        response.renderError(Status.STATUS_405);

    }

    /**
     * TRACE 请求
     *
     * @param request  :请求
     * @param response :响应
     * @throws IOException :遇到异常时抛出
     */
    public void doTrace(HttpRequest request, HttpResponse response) throws IOException {
        response.renderError(Status.STATUS_405);
    }
}
