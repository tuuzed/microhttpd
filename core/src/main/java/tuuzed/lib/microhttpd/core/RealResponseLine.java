package tuuzed.lib.microhttpd.core;

import org.jetbrains.annotations.NotNull;
import tuuzed.lib.microhttpd.Protocol;
import tuuzed.lib.microhttpd.ResponseLine;
import tuuzed.lib.microhttpd.StatusCode;

final class RealResponseLine implements ResponseLine {
    private Protocol protocol;
    private StatusCode sc;

    RealResponseLine(@NotNull Protocol protocol, @NotNull StatusCode sc) {
        this.protocol = protocol;
        this.sc = sc;
    }

    @Override
    public void setSC(@NotNull StatusCode sc) {
        this.sc = sc;
    }

    @Override
    public Protocol protocol() {
        return protocol;
    }

    @Override
    public StatusCode sc() {
        return sc;
    }

    @Override
    public String toString() {
        return protocol.protocolName() + "/" + protocol.version() + " " + sc.toString();
    }
}
