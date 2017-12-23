package org.gmjm.slack.reactive;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@CommonsLog
public class SlackSocketEventHandler extends AbstractWebSocketHandler {

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Object payload = message.getPayload();

        log.info(payload);
    }
}
