package it.cnr.istc.psts.Websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageHeaderInitializer;
import org.springframework.stereotype.Component;

@Component
public class Sending {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	private MessageHeaderInitializer headerInitializer;

	public  void notify(String message, String sessionId) {
		messagingTemplate.convertAndSendToUser(sessionId, "/queue/notify", message, createHeaders(sessionId));
		return;
	}

	private MessageHeaders createHeaders(String sessionId) {
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		headerAccessor.setSessionId(sessionId);
		headerAccessor.setLeaveMutable(true);
		return headerAccessor.getMessageHeaders();
	}

}
