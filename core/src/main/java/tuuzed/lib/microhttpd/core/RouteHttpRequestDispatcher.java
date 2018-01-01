package tuuzed.lib.microhttpd.core;

import org.jetbrains.annotations.NotNull;
import tuuzed.lib.microhttpd.Handler;
import tuuzed.lib.microhttpd.HttpRequest;
import tuuzed.lib.microhttpd.HttpRequestDispatcher;
import tuuzed.lib.microhttpd.HttpResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RouteHttpRequestDispatcher implements HttpRequestDispatcher {
    private final List<HandlerHolder> handlerHolders;
    private volatile boolean isSorted = false;

    public RouteHttpRequestDispatcher() {
        this.handlerHolders = new ArrayList<>();
    }

    @Override
    public HttpResponse dispatch(@NotNull HttpRequest req) {
        Handler handler = getHandler(req.requestLine().url());
        if (handler == null) return HttpResponses.error(StatusCodeEnum.STATUS_404);
        return handler.handle(req);
    }

    public RouteHttpRequestDispatcher addHandler(@NotNull Pattern urlPattern, @NotNull Handler handler) {
        return addHandler(urlPattern, 0, handler);
    }

    public RouteHttpRequestDispatcher addHandler(@NotNull Pattern urlPattern, int priority, @NotNull Handler handler) {
        HandlerHolder handlerHolder = new HandlerHolder(urlPattern, priority, handler);
        handlerHolders.add(handlerHolder);
        isSorted = false;
        return this;
    }

    public Handler removeHandler(Handler handler) {
        for (HandlerHolder handlerHolder : handlerHolders) {
            if (handler == handlerHolder.handler) {
                handlerHolders.remove(handlerHolder);
                return handlerHolder.handler;
            }
        }
        return null;
    }

    private Handler getHandler(String url) {
        if (!isSorted) {
            synchronized (handlerHolders) {
                handlerHolders.sort((o1, o2) -> Integer.compare(o2.priority, o1.priority));
            }
            isSorted = true;
        }
        for (HandlerHolder it : handlerHolders) {
            Matcher matcher = it.urlPattern.matcher(url);
            if (matcher.find()) {
                return it.handler;
            }
        }
        return null;
    }

    private static class HandlerHolder {
        private Pattern urlPattern;
        private int priority;
        private Handler handler;

        HandlerHolder(Pattern urlPattern, int priority, Handler handler) {
            this.urlPattern = urlPattern;
            this.priority = priority;
            this.handler = handler;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            HandlerHolder that = (HandlerHolder) o;

            if (priority != that.priority) return false;
            if (urlPattern != null ? !urlPattern.equals(that.urlPattern) : that.urlPattern != null) return false;
            return handler != null ? handler.equals(that.handler) : that.handler == null;
        }

        @Override
        public int hashCode() {
            int result = urlPattern != null ? urlPattern.hashCode() : 0;
            result = 31 * result + priority;
            result = 31 * result + (handler != null ? handler.hashCode() : 0);
            return result;
        }
    }
}
