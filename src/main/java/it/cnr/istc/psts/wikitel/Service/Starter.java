package it.cnr.istc.psts.wikitel.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.cnr.istc.psts.WikitelNewApplication;
import it.cnr.istc.psts.Websocket.Sending;
import it.cnr.istc.psts.wikitel.controller.LessonManager;
import it.cnr.istc.psts.wikitel.controller.MainController;
import it.cnr.istc.psts.wikitel.controller.UserController;
import it.cnr.istc.psts.wikitel.db.LessonEntity;
import it.cnr.istc.psts.wikitel.db.UserEntity;


@Component
@Order
public class Starter implements CommandLineRunner {

	@Autowired
	public LessonService lessonservice;
	
	
	@Autowired
	ModelService modelservice;
	
	@Autowired
	UserService userservice;
	
	@Autowired
	RuleService ruleservice;
	
	@Autowired
	private Sending send;
	
	public static final ObjectMapper mapper = new ObjectMapper();
	public static JsonNode USER_MODEL;
	public static final ScheduledExecutorService EXECUTOR = Executors
	            .newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
	public static final UserController userController = new UserController();

	@Override
	public void run(String... args) throws Exception {
		System.out.println("prova: "+Starter.class.getClassLoader().getResource("json/user_model.json"));
		USER_MODEL = mapper.readTree(Starter.class.getClassLoader().getResourceAsStream("json/user_model.json"));
		List<LessonEntity> lesson = lessonservice.all();
		List<UserEntity> user = new ArrayList<>();
		for(LessonEntity l : lesson) {
			user.addAll(l.getFollowed_by());
			l.getFollowed_by().clear();
			for(UserEntity u : user) {	
				l.getFollowed_by().add(u);
			System.out.println("added lesson:  " + l.getName());
			LessonManager manager = new LessonManager(l,send,modelservice,userservice,ruleservice);
			String n = String.valueOf(l.getId()) + String.valueOf(u.getId());
			MainController.LESSONS.put(n,manager);
			manager.Solve();
			l.getFollowed_by().clear();
			}
			l.getFollowed_by().addAll(user);
		}
		
	}

 
}
