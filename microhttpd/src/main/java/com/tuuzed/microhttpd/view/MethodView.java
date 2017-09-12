package com.tuuzed.microhttpd.view;

import com.tuuzed.microhttpd.http.Request;
import com.tuuzed.microhttpd.http.Method;
import com.tuuzed.microhttpd.http.Response;
import com.tuuzed.microhttpd.http.Status;
import com.tuuzed.microhttpd.common.util.Logger;

import java.io.IOException;

/**
 * Http请求处理
 */
public class MethodView implements View {
    private final static Logger logger = Logger.getLogger(MethodView.class);

    @Override
    public void serve(Request req, Response resp) throws IOException {
        logger.debug("Receive request..." + req.toString());
        String method = req.getMethod();
        switch (method) {
            case Method.GET:
                doGet(req, resp);
                break;
            case Method.POST:
                doPost(req, resp);
                break;
            case Method.PUT:
                doPut(req, resp);
                break;
            case Method.DELETE:
                doDelete(req, resp);
                break;
            case Method.PATCH:
                doPatch(req, resp);
                break;
            case Method.HEAD:
                doHead(req, resp);
                break;
            case Method.CONNECT:
                doConnect(req, resp);
                break;
            case Method.OPTIONS:
                doOptions(req, resp);
                break;
            case Method.TRACE:
                doTrace(req, resp);
                break;
        }
    }

    /**
     * GET 请求
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 遇到异常时抛出
     */
    public void doGet(Request req, Response resp) throws IOException {
        resp.renderError(Status.STATUS_405);
    }

    /**
     * POST 请求
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 遇到异常时抛出
     */
    public void doPost(Request req, Response resp) throws IOException {
        resp.renderError(Status.STATUS_405);
    }

    /**
     * PUT 请求
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 遇到异常时抛出
     */
    public void doPut(Request req, Response resp) throws IOException {
        resp.renderError(Status.STATUS_405);
    }

    /**
     * DELETE 请求
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 遇到异常时抛出
     */
    public void doDelete(Request req, Response resp) throws IOException {
        resp.renderError(Status.STATUS_405);
    }

    /**
     * PATCH 请求
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 遇到异常时抛出
     */
    public void doPatch(Request req, Response resp) throws IOException {
        resp.renderError(Status.STATUS_405);
    }

    /**
     * HEAD 请求
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 遇到异常时抛出
     */
    public void doHead(Request req, Response resp) throws IOException {
        resp.renderError(Status.STATUS_405);
    }

    /**
     * CONNECT 请求
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 遇到异常时抛出
     */
    public void doConnect(Request req, Response resp) throws IOException {
        resp.renderError(Status.STATUS_405);
    }

    /**
     * OPTIONS 请求
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 遇到异常时抛出
     */
    public void doOptions(Request req, Response resp) throws IOException {
        resp.renderError(Status.STATUS_405);
    }

    /**
     * TRACE 请求
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 遇到异常时抛出
     */
    public void doTrace(Request req, Response resp) throws IOException {
        resp.renderError(Status.STATUS_405);
    }
}
