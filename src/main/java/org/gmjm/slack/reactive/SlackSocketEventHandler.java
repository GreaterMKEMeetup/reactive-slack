package org.gmjm.slack.reactive;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

public class SlackSocketEventHandler extends AbstractWebSocketHandler {

    private WebSocketSession session;

    public void sendMessage(String payload) throws Exception {
        session.sendMessage(new TextMessage(payload));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        this.session = session;
    }
}
