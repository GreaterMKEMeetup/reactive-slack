package org.gmjm.slack.reactive;

import  org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Default implementation doesn't wait until socket session is established, so this implementation overrides
 * waits for a session for {@code timeout} seconds.
 * </p>
 *
 * {@code timeout} waits 10 seconds by default.
 */
public class SyncWebSocketConnectionManager extends WebSocketConnectionManager {

    private final WebSocketClient client;
    private final WebSocketHandler webSocketHandler;
    private final int timeout;

    private WebSocketSession webSocketSession;
    private WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    private static final int DEFAULT_TIMEOUT = 10;

    public SyncWebSocketConnectionManager(WebSocketClient client, WebSocketHandler webSocketHandler,
                                          String uriTemplate, Object... uriVariables) {
        this(client, webSocketHandler, uriTemplate, DEFAULT_TIMEOUT, uriVariables);
    }

    public SyncWebSocketConnectionManager(WebSocketClient client, WebSocketHandler webSocketHandler,
                                          String uriTemplate, int timeout, Object... uriVariables) {
        super(client, webSocketHandler, uriTemplate, uriVariables);
        this.client = client;
        this.webSocketHandler = webSocketHandler;
        this.timeout = timeout;
    }

    @Override
    protected void openConnection() {
        if (logger.isInfoEnabled()) {
            logger.info("Connecting to WebSocket at " + getUri());
        }

        ListenableFuture<WebSocketSession> future =
            this.client.doHandshake(this.webSocketHandler, this.headers, getUri());

        try {
            webSocketSession = future.get(timeout, TimeUnit.SECONDS);

            logger.info("Successfully connected to websocket");
        } catch (Exception e) {
            logger.error("Failed to connect", e);
            throw new IllegalStateException(e);
        }
    }
}
