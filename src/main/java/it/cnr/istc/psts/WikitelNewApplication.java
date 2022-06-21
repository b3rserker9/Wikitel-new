package it.cnr.istc.psts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import it.cnr.istc.psts.Websocket.Sending;
import it.cnr.istc.psts.wikitel.Service.LessonService;
import it.cnr.istc.psts.wikitel.Service.ModelService;
import it.cnr.istc.psts.wikitel.Service.Starter;
import it.cnr.istc.psts.wikitel.Service.UserService;
import it.cnr.istc.psts.wikitel.controller.LessonManager;
import it.cnr.istc.psts.wikitel.controller.MainController;
import it.cnr.istc.psts.wikitel.controller.UserController;
import it.cnr.istc.psts.wikitel.db.LessonEntity;

@SpringBootApplication

public class WikitelNewApplication {
	

	
	public static final ObjectMapper mapper = new ObjectMapper();
	
	public static final ScheduledExecutorService EXECUTOR = Executors
	            .newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
	public static final UserController userController = new UserController();

	public static void main(String[] args) throws IOException, BeansException{
		
		SpringApplication.run(WikitelNewApplication.class, args);
		
		ProcessBuilder builder = new ProcessBuilder("python3",  System.getProperty("user.dir") + "/wcb/wikitel.py");
		System.out.println(InetAddress.getLocalHost().getHostAddress());
		System.out.println(System.getProperty("user.dir") +"/wcb/wikitel.py");
		Process process = builder.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String lines=null;
		System.out.println("1: " +reader.readLine());
		System.out.println("Python Server is working: " + process);
		while((lines = reader.readLine())!=null) {
			System.out.println("python:  " + lines);
		}
		
		
		
	
		
	}
	


}
