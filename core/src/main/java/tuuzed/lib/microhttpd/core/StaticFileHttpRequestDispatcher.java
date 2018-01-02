package tuuzed.lib.microhttpd.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tuuzed.lib.microhttpd.HttpRequest;
import tuuzed.lib.microhttpd.HttpRequestDispatcher;
import tuuzed.lib.microhttpd.HttpResponse;
import tuuzed.lib.microhttpd.internal.URLEncoder;

import java.io.File;
import java.util.regex.Pattern;

public class StaticFileHttpRequestDispatcher implements HttpRequestDispatcher {
    private final static Pattern INDEX_FILE = Pattern.compile("^((index)|(default))\\.((htm)|(html))$");
    private String urlPrefix;
    private File path;
    private Pattern pattern;

    public StaticFileHttpRequestDispatcher(String urlPrefix, File path) {
        if (!urlPrefix.startsWith("/")) {
            urlPrefix = "/" + urlPrefix;
        }
        if (!urlPrefix.endsWith("/")) {
            urlPrefix += "/";
        }
        pattern = Pattern.compile("^" + urlPrefix + "*");
        this.urlPrefix = urlPrefix;
        this.path = path;
    }

    @Nullable
    @Override
    public HttpResponse dispatch(@NotNull HttpRequest req) {
        String url = req.requestLine().url();
        if (!url.startsWith(urlPrefix)) return null;
        String child = url.substring(urlPrefix.length());
        File file = new File(path, child);
        if (!file.exists()) {
            return HttpResponses.error(StatusCodeEnum.STATUS_404);
        }
        String dirname;
        if (child.isEmpty()) {
            dirname = File.separator;
        } else {
            dirname = file.getPath().substring(path.getPath().length());
        }
        if (file.isFile()) {
            return HttpResponses.file(file);
        }
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<title>").append("Directory listing for ").append(dirname).append("</title>");
        html.append("</head>");
        html.append("<body>");
        html.append("<h1>").append("Directory listing for ").append(dirname).append("</h1>");
        html.append("<hr/>");
        html.append("<ul>");
        if (!file.getPath().equals(path.getPath())) {
            html.append("<li><a href=\"../\">../</a><br/></li>");
        }
        File[] files = file.listFiles();
        if (files != null && files.length != 0) {
            for (File f : files) {
                String filename = f.getName();
                if (INDEX_FILE.matcher(f.getName()).find() && f.isFile()) {
                    return HttpResponses.file(f);
                }
                html.append("<li><a href=\"")
                        .append("./")
                        .append(URLEncoder.encode(filename));
                if (f.isDirectory()) html.append("/");
                html.append("\">").append(filename);
                if (f.isDirectory()) html.append("/");
                html.append("</a><br/></li>");

            }
        }
        html.append("</ul>");
        html.append("<hr/>");
        html.append("</body>");
        html.append("</html>");
        return HttpResponses.html(html.toString());
    }
}
