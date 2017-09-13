package com.tuuzed.microhttpd.route;

import com.tuuzed.microhttpd.view.View;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Routes {
    private static Routes defaultInstance;
    private final List<ViewRoute> viewRoutes;
    private boolean isSorted;

    public Routes() {
        this.viewRoutes = new LinkedList<>();
    }

    public void registerView(@NotNull String route, @NotNull View view, int priority) {
        for (int i = 0; i < viewRoutes.size(); i++) {
            if (route.equals(viewRoutes.get(i).getRoutePattern().pattern())) {
                viewRoutes.remove(i);
            }
        }
        isSorted = false;
        ViewRoute viewRoute = new ViewRoute(Pattern.compile(route), view, priority);
        viewRoutes.add(viewRoute);
    }

    public static Routes getDefault() {
        if (defaultInstance == null) {
            synchronized (Routes.class) {
                if (defaultInstance == null) {
                    defaultInstance = new Routes();
                }
            }
        }
        return defaultInstance;
    }

    @Nullable
    public View getView(@NotNull String url) {
        if (!isSorted) {
            synchronized (viewRoutes) {
                Collections.sort(viewRoutes, new Comparator<ViewRoute>() {
                    @Override
                    public int compare(ViewRoute o1, ViewRoute o2) {
                        if (o1.getPriority() > o2.getPriority()) return -1;
                        else if (o1.getPriority() < o2.getPriority()) return 1;
                        else return 0;
                    }
                });
            }
        }
        for (ViewRoute viewRoute : viewRoutes) {
            Matcher matcher = viewRoute.getRoutePattern().matcher(url);
            if (matcher.find()) {
                return viewRoute.getView();
            }
        }
        return null;
    }

}
