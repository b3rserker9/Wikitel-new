package it.cnr.istc.psts.wikitel.controller;


import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.socket.WebSocketSession;

import lombok.Data;

@Data
public class UserController {
	 @Autowired
	 private SimpMessagingTemplate simpMessagingTemplate;
	public static final Map<Long,String> ONLINE = new HashMap<>();
	
	
	
	

}
