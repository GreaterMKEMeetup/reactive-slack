package org.gmjm.slack.reactive;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReactiveSlackApplicationTests {

	@Autowired
	private SlackSocketEventHandler eventHandler;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void contextLoads() {
		assertNotNull(eventHandler);
	}

	@Test
	public void sendMessage() throws Exception {
		Map<String, Object> payload = new HashMap<>();
		payload.put("id", 1);
		payload.put("channel", "C0VPRF1HU");
		payload.put("type", "message");
		payload.put("text", "hello from JUnit");

		String payloadStr = objectMapper.writeValueAsString(payload);

		eventHandler.sendMessage(payloadStr);
	}

}
