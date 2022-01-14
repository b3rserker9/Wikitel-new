package it.cnr.istc.psts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.cnr.istc.psts.wikitel.controller.LessonManager;

@SpringBootApplication
public class WikitelNewApplication {
	
	public static final ObjectMapper mapper = new ObjectMapper();
	public static JsonNode USER_MODEL;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(WikitelNewApplication.class, args);
		ProcessBuilder builder = new ProcessBuilder("python",  "C:\\Users\\utente\\Documents\\workspace-spring-tool-suite-4-4.11.1.RELEASE\\Wikitel-new\\wcb\\wikitel.py");
		Process process = builder.start();
		System.out.print(WikitelNewApplication.class.getClassLoader().getResourceAsStream("\\json\\user_model.json"));
		 USER_MODEL = mapper.readTree(WikitelNewApplication.class.getClassLoader().getResourceAsStream("\\json\\user_model.json"));
		System.out.println(USER_MODEL);
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		String lines=null;
		while((lines = reader.readLine())!=null) {
			System.out.println("python:  " + lines);
		}
	}

}
