package org.gmjm.slack.reactive;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class WebSocketConfiguration {

    private static final String URI_TEMPLATE = "https://{slackHost}/api/rtm.connect?token={appToken}";

    @Value("${slack.host}")
    private String slackHost;

    @Value("${app.token}")
    private String appToken;

    @Bean
    public SlackSocketEventHandler slackSocketEventHandler() {
        return new SlackSocketEventHandler();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebSocketClient client() {
        return new StandardWebSocketClient();
    }

    @Bean
    public SyncWebSocketConnectionManager webSocketSession(SlackSocketEventHandler slackSocketEventHandler, WebSocketClient client, RestTemplate restTemplate) {

        ResponseEntity<SlackConnectResponse> response = restTemplate.getForEntity(URI_TEMPLATE, SlackConnectResponse.class, slackHost, appToken);

        Assert.isTrue(response.getStatusCode().is2xxSuccessful() && response.getBody().ok, "Connect Request Failed");

        SlackConnectResponse connectResponse = response.getBody();

        SyncWebSocketConnectionManager connectionManager = new SyncWebSocketConnectionManager(client, slackSocketEventHandler, connectResponse.url);
        connectionManager.setAutoStartup(true);

        return connectionManager;
    }


    @Data
    public static class SlackConnectResponse {

        private boolean ok;
        private String url;
        private SlackSelf id;

    }

    @Data
    public static class SlackSelf {
        private String id;
        private String name;
    }

}
