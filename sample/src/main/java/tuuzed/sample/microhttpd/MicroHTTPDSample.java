package tuuzed.sample.microhttpd;

import tuuzed.lib.microhttpd.MicroHTTPD;
import tuuzed.lib.microhttpd.core.HttpResponses;
import tuuzed.lib.microhttpd.core.RouteHttpRequestDispatcher;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MicroHTTPDSample {

    public static void main(String[] args) {
        RouteHttpRequestDispatcher dispatcher = new RouteHttpRequestDispatcher()
                .addHandler(Pattern.compile("^/req$"), req -> {
                    return HttpResponses.text(req.toString());
                })
                .addHandler(Pattern.compile("^/$"), req -> {
                    return HttpResponses.text("hello every one");
                })
                .addHandler(Pattern.compile("^/mimetypes/*$"), req -> {
                    URL resource = MicroHTTPD.class.getResource("/mimetypes.txt");
                    File file = new File(resource.getFile());
                    return HttpResponses.file(file);
                })
                .addHandler(Pattern.compile("^/redirect/*$"), req -> {
                    return HttpResponses.redirect_301("//localhost:8000");
                })
                .addHandler(Pattern.compile("^/redirect2/*$"), req -> {
                    return HttpResponses.redirect_301("/mimetypes");
                });
        MicroHTTPD.builder()
                .addHttpRequestDispatcher(dispatcher)
                .useStaticFileHttpRequestDispatcher("/static/", new File("C:\\"))
                .setTimeout(1000, TimeUnit.MILLISECONDS)
                .build()
                .start();
    }
}
