package com.tuuzed.microhttpd.http;

import org.jetbrains.annotations.NotNull;

public class Protocol {
    private String name;
    private String version;

    public Protocol(@NotNull String name, @NotNull String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "Protocol{" +
                "name=" + name +
                ", version=" + version + '}';
    }
}
