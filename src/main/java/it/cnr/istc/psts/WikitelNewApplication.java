package it.cnr.istc.psts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.cnr.istc.psts.wikitel.controller.LessonManager;
import it.cnr.istc.psts.wikitel.controller.UserController;

@SpringBootApplication
public class WikitelNewApplication {
	


	public static void main(String[] args) throws IOException{
		
		SpringApplication.run(WikitelNewApplication.class, args);
		ProcessBuilder builder = new ProcessBuilder("python",  System.getProperty("user.dir")+"\\wcb\\wikitel.py");
		System.out.println(InetAddress.getLocalHost().getHostAddress());
		Process process = builder.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String lines=null;
		while((lines = reader.readLine())!=null) {
			System.out.println("python:  " + lines);
		}
	
		
	}

}
