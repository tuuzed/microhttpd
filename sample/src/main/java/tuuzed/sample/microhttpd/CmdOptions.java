package tuuzed.sample.microhttpd;

import org.kohsuke.args4j.Option;

public class CmdOptions {
    @Option(name = "-p", aliases = {"--port"}, usage = "--port PORT , -p PORT" +
            "\nSpecify alternate port [default: 4000]")
    public int port;
    @Option(name = "-d", aliases = {"--dir"}, usage = "--dir PATH , -d PATH" +
            "\nSpecify directory path [default: current directory]")
    public String dir;
    @Option(name = "-b", aliases = {"--bind"}, usage = "--bind ADDRESS , -b ADDRESS" +
            "\nSpecify alternate bind address [default: all interfaces]")
    public String bind;
}
