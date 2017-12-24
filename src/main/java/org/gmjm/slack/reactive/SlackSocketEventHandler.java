package org.gmjm.slack.reactive;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import javax.annotation.PreDestroy;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@WebSocket
@CommonsLog
@RequiredArgsConstructor
public class SlackSocketEventHandler {

    private final CountDownLatch closeLatch = new CountDownLatch(1);

    private final int duration = 10;
    private final TimeUnit timeUnit = TimeUnit.SECONDS;

    private Session session;

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;

        log.info("Connected to socket session");
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        log.info("close triggered");
        this.session = null;
        this.closeLatch.countDown();
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        log.info(msg);
    }

    @PreDestroy
    public void close() {
        log.info("closing connection");
        try {
            this.closeLatch.await(duration, timeUnit);
            log.info("connection closed");
        } catch (Exception e) {
            log.error("connection did not close cleanly", e);
        }
    }
}
