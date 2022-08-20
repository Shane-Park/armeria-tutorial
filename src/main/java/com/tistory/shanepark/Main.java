package com.tistory.shanepark;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.docs.DocService;
import com.tistory.shanepark.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    static Server newServer(int port) {
        DocService docService = DocService.builder()
                .exampleRequests(BoardService.class,
                        "createBoard",
                        "{\"title\":\"My first board\", \"content\":\"Hello Armeria!\"}")
                .build();

        return Server.builder()
                .http(port)
                .annotatedService(new BoardService())
                .serviceUnder("/docs", docService)
                .build();
    }

    public static void main(String[] args) {
        Server server = newServer(8080);
        server.closeOnJvmShutdown();
        server.start().join();
        log.info("Server has been started on http://localhost:{}", server.activeLocalPort());
        log.info("Serving DocService at http://localhost:{}/docs", server.activeLocalPort());
    }

}
