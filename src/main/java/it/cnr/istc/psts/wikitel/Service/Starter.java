package it.cnr.istc.psts.wikitel.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.cnr.istc.psts.WikitelNewApplication;
import it.cnr.istc.psts.Websocket.Sending;
import it.cnr.istc.psts.wikitel.controller.LessonManager;
import it.cnr.istc.psts.wikitel.controller.MainController;
import it.cnr.istc.psts.wikitel.controller.UserController;
import it.cnr.istc.psts.wikitel.db.LessonEntity;


@Service
@Configurable
@Transactional
public class Starter{

	@Autowired
	public LessonService lessonservice;
	
	
	@Autowired
	ModelService modelservice;
	
	@Autowired
	UserService userservice;
	
	@Autowired
	private Sending send;
	
	public static final ObjectMapper mapper = new ObjectMapper();
	public static JsonNode USER_MODEL;
	public static final ScheduledExecutorService EXECUTOR = Executors
	            .newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
	public static final UserController userController = new UserController();

	@PostConstruct
	
	public void start() throws IOException {
		 USER_MODEL = mapper.readTree(Starter.class.getClassLoader().getResourceAsStream("\\json\\user_model.json"));
			
			
			
		List<LessonEntity> lesson = this.lessonservice.all();
		for(LessonEntity l : lesson) {
			System.out.println("added lesson:  " + l.getName());
			LessonManager manager = new LessonManager(l,send,modelservice,userservice);
			MainController.LESSONS.put(l.getId(),manager);
			manager.Solve();
		}
	
	
	}

 
}