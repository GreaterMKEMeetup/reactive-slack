package org.gmjm.slack.reactive;

import lombok.Data;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Configuration
public class WebSocketConfiguration {

    private static final String URI_TEMPLATE = "https://{slackHost}/api/rtm.connect?token={appToken}";

    @Value("${slack.host}")
    private String slackHost;

    @Value("${app.token}")
    private String appToken;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SlackSocketEventHandler webSocket(RestTemplate restTemplate, WebSocketClient webSocketClient) throws Exception {
        ResponseEntity<SlackConnectResponse> response = restTemplate.getForEntity(URI_TEMPLATE, SlackConnectResponse.class, slackHost, appToken);

        Assert.isTrue(response.getStatusCode().is2xxSuccessful() && response.getBody().ok, "Connect Request Failed");

        SlackConnectResponse connectResponse = response.getBody();

        ClientUpgradeRequest request = new ClientUpgradeRequest();
        SlackSocketEventHandler eventHandler = new SlackSocketEventHandler();

        webSocketClient.connect(eventHandler, new URI(connectResponse.url), request);

        return eventHandler;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WebSocketClient webSocketClient()  {
        return new WebSocketClient();
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
