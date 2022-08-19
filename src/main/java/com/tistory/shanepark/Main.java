package com.tistory.shanepark;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    static Server newServer(int port) {
        ServerBuilder builder = Server.builder();
        return builder.http(port)
                .service("/", (ctx, req) -> HttpResponse.of("Hello, Armeria!"))
                .build();
    }

    public static void main(String[] args) {
        Server server = newServer(8080);
        server.closeOnJvmShutdown();
        server.start().join();
        log.info("Server has been started on http://localhost:{}", server.activeLocalPort());
    }

}
