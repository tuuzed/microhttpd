package tuuzed.lib.microhttpd.core;

import tuuzed.lib.microhttpd.Protocol;

public enum ProtocolEnum implements Protocol {

    HTTP_1_0("HTTP", "1.0"),
    HTTP_1_1("HTTP", "1.1");

    private String protocolName;
    private String version;


    ProtocolEnum(String protocolName, String version) {
        this.protocolName = protocolName;
        this.version = version;
    }

    @Override
    public String protocolName() {
        return protocolName;
    }

    @Override
    public String version() {
        return version;
    }
}
