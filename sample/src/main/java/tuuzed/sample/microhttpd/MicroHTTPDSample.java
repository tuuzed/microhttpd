package tuuzed.sample.microhttpd;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import tuuzed.lib.microhttpd.MicroHTTPD;
import tuuzed.lib.microhttpd.core.HttpResponses;
import tuuzed.lib.microhttpd.core.RouteHttpRequestDispatcher;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class MicroHTTPDSample {

    public static void main(String[] args) {
        Logger.getGlobal().setLevel(Level.CONFIG);
        CmdOptions options = new CmdOptions();
        CmdLineParser parser = new CmdLineParser(options);
        if (args.length == 0) {
            System.out.println("Usage: java -jar microhttpd [-b] [-p] [-d]");
            parser.printUsage(System.out);
        } else {
            try {
                parser.parseArgument(args);
            } catch (CmdLineException e) {
                e.printStackTrace();
            }
        }
        if (options.port == 0) {
            options.port = 4000;
        }
        if (options.dir == null) {
            options.dir = new File("").getAbsolutePath();
        }
        if (options.bind == null) {
            options.bind = "0.0.0.0";
        }
        RouteHttpRequestDispatcher dispatcher = new RouteHttpRequestDispatcher()
                // 首页
                .addHandler(Pattern.compile("^/*$"), req -> HttpResponses.text("hello everyone"))
                // 获取Request信息
                .addHandler(Pattern.compile("^/req/*$"), req -> HttpResponses.text(req.toString()))
                // 重定向
                .addHandler(Pattern.compile("^/baidu/*$"), req -> HttpResponses.redirect_302("http://www.baidu.com"));
        MicroHTTPD.builder()
                .addHttpRequestDispatcher(dispatcher)
                .setListenPort(options.port)
                .setListenHostname(options.bind)
                .useStaticFileHttpRequestDispatcher("/static/", new File(options.dir))
                .setTimeout(1000, TimeUnit.MILLISECONDS)
                .build()
                .start();
    }
}
