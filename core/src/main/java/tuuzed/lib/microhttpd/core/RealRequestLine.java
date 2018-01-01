package tuuzed.lib.microhttpd.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tuuzed.lib.microhttpd.Protocol;
import tuuzed.lib.microhttpd.QueryParams;
import tuuzed.lib.microhttpd.RequestLine;
import tuuzed.lib.microhttpd.internal.URLDecoder;


final class RealRequestLine implements RequestLine {
    private String rawRequestLine;
    private String method;
    private String url;
    private Protocol protocol;
    private QueryParams queryParams;

    private RealRequestLine(String rawRequestLine,
                            String method,
                            String url,
                            Protocol protocol,
                            QueryParams queryParams) {
        this.rawRequestLine = rawRequestLine;
        this.method = method;
        this.url = url;
        this.protocol = protocol;
        this.queryParams = queryParams;
    }

    @Override
    public String method() {
        return method;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public Protocol protocol() {
        return protocol;
    }

    @Override
    @Nullable
    public QueryParams queryParams() {
        return queryParams;
    }

    @Override
    public String rawRequestLine() {
        return rawRequestLine;
    }

    @Override
    public String toString() {
        return rawRequestLine;
    }

    /**
     * @param requestLine GET /uri?hello=world&name=tom HTTP/1.1
     * @return
     */
    @Nullable
    static RequestLine of(@NotNull String requestLine) {
        String[] tmpArray;
        tmpArray = requestLine.split(" ");
        if (tmpArray.length != 3) return null;
        String method = tmpArray[0];
        String uri = tmpArray[1];
        tmpArray = tmpArray[2].split("/");
        if (tmpArray.length != 2) return null;
        final String protocolName = tmpArray[0].toUpperCase();
        final String protocolVersion = tmpArray[1];
        int index = uri.indexOf('?');
        QueryParams queryParams = null;
        if (index == -1) {
            uri = URLDecoder.decode(uri);
        } else {
            String uriLine = uri.substring(0, index);
            String paramLine = uri.substring(index + 1);
            uri = URLDecoder.decode(uriLine);
            String[] params = paramLine.split("&");
            queryParams = new RealQueryParams(params.length);
            for (String param : params) {
                index = param.indexOf('=');
                if (index == -1) {
                    String key = URLDecoder.decode(param);
                    queryParams.put(key, "");
                } else {
                    String key = param.substring(0, index);
                    String value = param.substring(index + 1);
                    queryParams.put(URLDecoder.decode(key), URLDecoder.decode(value));
                }
            }
        }
        Protocol protocol;
        if ("HTTP".equals(protocolName)) {
            if ("1.0".equals(protocolVersion)) {
                protocol = ProtocolEnum.HTTP_1_0;
            } else if ("1.1".equals(protocolVersion)) {
                protocol = ProtocolEnum.HTTP_1_1;
            } else {
                protocol = new Protocol() {
                    @Override
                    public String protocolName() {
                        return protocolName;
                    }

                    @Override
                    public String version() {
                        return protocolVersion;
                    }
                };
            }
        } else {
            return null;
        }
        return new RealRequestLine(requestLine, method, uri, protocol, queryParams);
    }

}
