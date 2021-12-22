package it.cnr.istc.psts.wikitel.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import it.cnr.istc.psts.wikitel.db.Email;
import it.cnr.istc.psts.wikitel.db.LessonEntity;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import it.cnr.istc.psts.wikitel.Repository.LessonsRepository;
import it.cnr.istc.psts.wikitel.Repository.Response;
import it.cnr.istc.psts.wikitel.Repository.UserRepository;
import it.cnr.istc.psts.wikitel.Service.LessonService;
import it.cnr.istc.psts.wikitel.Service.UserService;
import it.cnr.istc.psts.wikitel.controller.pageController;
import it.cnr.istc.psts.wikitel.db.User;
import it.cnr.istc.psts.wikitel.db.UserEntity;

@RestController
public class MainController {
	
	  @Autowired
	    protected PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userrepository;
	
	@Autowired
	private UserService userservice;
	
	@Autowired
	private LessonsRepository lessonrepository;
	
	@Autowired
	private LessonService lessonservice;
	
	private LessonEntity l;
	
	
	@PostMapping("/register")
	public Response register(@RequestBody User user) throws JsonGenerationException, JsonMappingException, IOException{
		System.out.println("entratoodòjfkòjòfdaeHJLFJ");
		Json_reader interests=new Json_reader();
		
		Response response = new Response("Done", user);
		UserEntity nuovo = new UserEntity();
		nuovo.setEmail(user.getEmail());
		nuovo.setFirst_name(user.getFirst_name());
		nuovo.setLast_name(user.getLast_name());
		nuovo.setPassword(this.passwordEncoder.encode(user.getPassword()));
		nuovo.setRole(nuovo.STUDENT_ROLE);
		interests.setInterests(user.getInterests());
		userrepository.save(nuovo);
		System.out.println(user.getEmail());
		ObjectMapper  mapper = new ObjectMapper();
		mapper.writeValue(new File("src\\main\\resources\\json\\user_" + userservice.getUser(nuovo.getEmail()).getId() + ".json"),interests);
		return response;
		
	}
	
	@PostMapping("/edit")
	public Response edit(@RequestBody User user){
		Response response = new Response("Done", user);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	System.out.println("username2:PIPPO");
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
		nuovo.setEmail(user.getEmail());
		nuovo.setFirst_name(user.getFirst_name());
		nuovo.setLast_name(user.getLast_name());
		userrepository.save(nuovo);
		
		System.out.println(nuovo.getPassword());
		
		return response;
		
	}
	
	@RequestMapping(value = "/Postlessons",  method = RequestMethod.POST)
	public Response lessons(@RequestBody String lesson){	
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
    	LessonEntity ls = new LessonEntity();
    	ls.setName(lesson);
    	ls.setTeacher(nuovo);
    	
    	lessonservice.save(ls); 	
    	this.l = ls;
		Response response = new Response("Done",ls);
		return response;
		
	}
	
	@PostMapping("/iscrizione")
	public Response iscrizione(@RequestBody Long id){
		Response response = new Response("Done");
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
    	LessonEntity l = lessonservice.lezionePerId(id);
    	if(nuovo.getFollowing_lessons().contains(l)) {
    		response.setStatus("exist");
    	}else {
    	l.addStudent(nuovo);
    	nuovo.getFollowing_lessons().add(l);
    	userservice.saveUser(nuovo);
    	this.l=l;
    	}
		
		
		
		return response;
		
	}

	@GetMapping("/Getlessons")
	public Response getAlllessons(){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
    	List<LessonEntity> l = lessonservice.getlesson(nuovo);
		Response response = new Response("Done",l);			
		return response;
		
	}
	@GetMapping("/Getlessons_student")
	public Response Getlessons_student(){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
    	List<LessonEntity> l = nuovo.getFollowing_lessons();
		Response response = new Response("Done",l);			
		return response;
		
	}
	
	@GetMapping("lezione/lessons")
	public Response getlessons2(){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
		LessonEntity n= new LessonEntity();
		n.setName("pippo");
		List<LessonEntity> l = new ArrayList<LessonEntity>();
		l.addAll(lessonservice.getlesson(nuovo));

		
		Response response = new Response("Done",l);			
		return response;
		
	}
	
	@GetMapping("/lesson")
	public Response getlesson(){	
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
		Response response = new Response("Done",this.l);	
		return response;
		
	}
	
	@PostMapping("/mauro")
	public Response email(@RequestBody Email email){
		System.out.println("username2:PIPPO");
		Response response = new Response("Done", email);
		if(userservice.getUser(email.getEmail()) == null) {
			response.setStatus("Done");
		}else {
			response.setStatus("No");
		}
		
		return response;
		
	}
	
	
	
	
}
