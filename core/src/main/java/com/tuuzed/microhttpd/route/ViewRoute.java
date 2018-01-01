package com.tuuzed.microhttpd.route;

import com.tuuzed.microhttpd.view.View;

import java.util.regex.Pattern;

public class ViewRoute {
    private Pattern routePattern;
    private View view;
    private int priority;


    public ViewRoute(Pattern routePattern, View view, int priority) {
        this.routePattern = routePattern;
        this.view = view;
        this.priority = priority;
    }

    public Pattern getRoutePattern() {
        return routePattern;
    }

    public View getView() {
        return view;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int hashCode() {
        return routePattern.pattern().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof ViewRoute
                && this.routePattern.pattern().equals(((ViewRoute) obj).routePattern.pattern());
    }

    @Override
    public String toString() {
        return "ViewRoute{" +
                "routePattern=" + routePattern +
                ", view=" + view +
                ", priority=" + priority +
                '}';
    }
}
