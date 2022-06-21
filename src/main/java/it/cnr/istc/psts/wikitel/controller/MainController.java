package it.cnr.istc.psts.wikitel.controller;


import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import it.cnr.istc.psts.wikitel.db.*;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageHeaderInitializer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;



import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.cnr.istc.psts.wikitel.db.*;
import it.cnr.istc.psts.Websocket.Sending;
import it.cnr.istc.psts.wikitel.Repository.ModelRepository;
import it.cnr.istc.psts.wikitel.Repository.ProvaMongoRepository;
import it.cnr.istc.psts.wikitel.Repository.Response;

import it.cnr.istc.psts.wikitel.Repository.UserRepository;
import it.cnr.istc.psts.wikitel.Service.CredentialService;
import it.cnr.istc.psts.wikitel.Service.FilesService;
import it.cnr.istc.psts.wikitel.Service.LessonService;
import it.cnr.istc.psts.wikitel.Service.ModelService;
import it.cnr.istc.psts.wikitel.Service.RuleSuggestionRelationService;
import it.cnr.istc.psts.wikitel.Service.Starter;
import it.cnr.istc.psts.wikitel.Service.UserService;
import it.cnr.psts.wikitel.API.Message;

@RestController
public class MainController {
	
	static final Logger LOG = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
 protected PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userrepository;
	
	@Autowired
	private ProvaMongoRepository mongo;
	
	@Autowired
	private UserService userservice;
	
	@Autowired
	private CredentialService credentialservice;
		
	@Autowired
	private LessonService lessonservice;
	
	@Autowired
	private FilesService fileservice;
	
	
	@Autowired
	private ModelService  modelservice;
	@Autowired
	private ModelRepository  modelrepository;
	
	@Autowired
	private Sending send;
	
	@Autowired
	private RuleSuggestionRelationService relationservice;
	 
	private LessonEntity l;
	
	private ModelEntity m;
	
	
	public static final Map<Long, LessonManager> LESSONS = new HashMap<>();
	
	
	@PostMapping("/register")
	public Response register(@RequestBody  ObjectNode node) throws JsonGenerationException, JsonMappingException, IOException{
		Json_reader interests=new Json_reader();
		Response response = new Response("Done");
		UserEntity nuovo = new UserEntity();
		Credentials newCred = new Credentials();
		newCred.setEmail(node.get("email").asText());
		newCred.setPassword(this.passwordEncoder.encode(node.get("password").asText()));
		newCred.setRole(node.get("role").asText());
		nuovo.setFirst_name(node.get("first_name").asText());
		nuovo.setLast_name(node.get("last_name").asText());
		
		nuovo.setProfile(node.get("profile").asText());
		nuovo.setSrc(node.get("src").asText());
		
		nuovo.setQuestionario(node.get("one").asText());
		newCred.setUser(nuovo);
		credentialservice.save(newCred);
		return response;
		
	}
	@MessageMapping("/register")
	public void prova(@Payload Session session, SimpMessageHeaderAccessor headerAccessor ) throws JsonProcessingException {
		UserController.ONLINE.put(session.getUser_id(), session.getSession());
		List<LessonEntity> lesson = this.lessonservice.getlesson(this.userservice.getUserId(session.getUser_id()));
		if(session.getLesson_id()!=null) {
		LessonManager manager = MainController.LESSONS.get(session.getLesson_id());
		send.notify(Starter.mapper.writeValueAsString( new LessonManager.Timelines(session.getLesson_id(), manager.getSolver().getTimelines())), session.getSession());
		send.notify(Starter.mapper.writeValueAsString(new LessonManager.Tick(session.getLesson_id(), manager.getCurrentTime())), session.getSession());
		if(manager.st!=null) {
		send.notify(Starter.mapper.writeValueAsString(MainController.LESSONS.get(session.getLesson_id()).st), UserController.ONLINE.get(session.getUser_id()));	
		}
		}
		
		
	}
	 
	
	@PostMapping("/getstimulus")
	public String stimulus(@RequestBody ObjectNode node) {
		
		
		return"Done";
	}
	
	@PostMapping("/getprofile")
	public Response getProfile(@RequestBody ObjectNode node) throws JsonMappingException, JsonProcessingException {
		UserEntity nuovo =  userservice.getUserId(node.get("id").asLong());
		Response response = new Response( nuovo.getProfile());
		System.out.println();
		return response;
	}
	
	
	@PostMapping("/findsuggestion")
	public Collection<RuleEntity> GetSuggestion(@RequestBody ObjectNode node) {
		System.out.println(node.get("ids").asLong());
		ModelEntity model = this.modelservice.getModel(node.get("ids").asLong());
		
		return model.getRules();
	}
	
	@PostMapping("/edit")
	public Response edit(@RequestBody User user){
		Response response = new Response("Done", user);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity nuovo = credentials.getUser();
		credentials.setEmail(user.getEmail());
		nuovo.setFirst_name(user.getFirst_name());
		nuovo.setLast_name(user.getLast_name());
		credentialservice.save(credentials);
		
		return response;
		
	}
	
	@PostMapping("/mongo")
	public String edit2(){
		mongo.save(new Provamongo("funge"));
		System.out.println("mongo funziona??");
		return "prova";
		
	}

	
	@PostMapping("/edit_interests")
	public Response edit_interests(@RequestBody User user) throws JsonGenerationException, JsonMappingException, IOException{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity nuovo = credentials.getUser();
    	nuovo.setProfile(user.getProfile());
    	userservice.saveUser(nuovo);
    	Response response = new Response("Done", nuovo);
    	ObjectMapper mapper = new ObjectMapper();
    	List<String> profile = mapper.readValue(nuovo.getProfile(), new TypeReference<List<String>>(){});
    	System.out.println("ciao " +profile.get(0));
		return response;
		
	}
	

	
	@PostMapping("/uploadFileString")
	public Response uploadFileString(@RequestBody ObjectNode node ) throws IllegalStateException, IOException{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity nuovo = credentials.getUser();
    	System.out.println(node.get("src").asText());
    	nuovo.setSrc(node.get("src").asText());
    	this.userservice.saveUser(nuovo);
    	Response response = new Response("Done");
		return response;
		
	}
	
	@PostMapping("/uploadFileRule")
	public Long uploadfilerule(@RequestBody MultipartFile uploadfile ) throws IllegalStateException, IOException{
		
		String file1 =  uploadfile.getOriginalFilename();
    	System.out.println(file1);
    	String baseDir=System.getProperty("user.dir")+"\\FileRule\\";
    	System.out.println(baseDir);
    	uploadfile.transferTo(new File(baseDir + file1));
    	RuleEntity rule = new FileRuleEntity();   	
     	((FileRuleEntity) rule).setSrc(baseDir +file1);
     	rule.setName(m.getName());
     	this.modelservice.saverule(rule);
     	this.m.addRule(rule);
     	this.modelservice.save(m);
    	
		return m.getId();
		
	}
	
	@PostMapping("/uploadFileLesson/{id}")
	public Files uploadfilelesson(@RequestBody MultipartFile uploadfile, @PathVariable("id") Long id ) throws IllegalStateException, IOException{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity nuovo = credentials.getUser();
		String file1 =  uploadfile.getOriginalFilename();
    	String baseDir=System.getProperty("user.dir")+"\\MaterialeDidattico\\";
    	Files f= new Files(file1);
    	this.fileservice.save(f);
    	uploadfile.transferTo(new File(baseDir + file1));
    	f.setSrc(baseDir + file1);
    	this.fileservice.save(f);
    	System.out.println("LESSOn ID: " + id);
    	LessonEntity lession = this.lessonservice.lezionePerId(id);
    	lession.getFiles().add(f);
    	this.lessonservice.save(lession);
    	
		return f;
		
	}
	
	
	@RequestMapping("/file/{id}")
		  public ResponseEntity<InputStreamResource> downloadFile1(
				  @PathVariable("id") Long id) throws IOException {

				
		        File file = new File(this.fileservice.filePerId(id).getSrc());
		        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		        return ResponseEntity.ok()
		                // Content-Disposition
		                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
		           
		                // Contet-Length
		                .contentLength(file.length()) //
		                .body(resource);
		    }
	
	@RequestMapping("/riddle/{id}")
	  public ResponseEntity<InputStreamResource> downloadFileRiddle(
			  @PathVariable("id") Long id) throws IOException {

			
	        File file = new File(System.getProperty("user.dir")+"\\riddle\\" + id + ".rddl");
	        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

	        return ResponseEntity.ok()
	                // Content-Disposition
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
	           
	                // Contet-Length
	                .contentLength(file.length()) //
	                .body(resource);
	    }
	
	
	
	@PostMapping("/uploadFile")
	public Response uploadfile(@RequestBody MultipartFile uploadfile ) throws IllegalStateException, IOException{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity nuovo = credentials.getUser();
		String file1 =  uploadfile.getOriginalFilename();
    	System.out.println(file1);
    	String baseDir=System.getProperty("user.dir")+"\\src\\main\\resources\\static\\images\\";
    	uploadfile.transferTo(new File(baseDir + nuovo.getId() +".jpg"));
    	nuovo.setSrc("\\images\\" + nuovo.getId() + ".jpg");
    	this.userservice.saveUser(nuovo);
    	Response response = new Response("Done");
		return response;
		
	}
	

	
	
	@PostMapping("/play")
	public Response PlayLesson(@RequestBody ObjectNode node ) throws IllegalStateException, IOException{
    	LESSONS.get(node.get("id").asLong()).play();
    	Response response = new Response("Done");
		return response;
		
	}
	@PostMapping("/pause")
	public Response pauseLesson(@RequestBody ObjectNode node ) throws IllegalStateException, IOException{
    	LESSONS.get(node.get("id").asLong()).pause();
   
  
    	Response response = new Response("Done");
		return response;
		
	}
	
	@PostMapping("/stop")
	public Response stopLesson(@RequestBody ObjectNode node ) throws IllegalStateException, IOException{

		LESSONS.get(node.get("id").asLong()).stop();

    
    	Response response = new Response("Done");
		return response;
		
	}
	
	
	
	@PostMapping("/Argomento/Getmodel")
	public ModelEntity getmodel(@RequestBody ObjectNode node){
		return this.modelservice.getModel(node.get("ids").asLong());
		
	}
	
	@PostMapping("/NewModel")
	public Response NewModel(@RequestBody String Model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity nuovo = credentials.getUser();
    	ModelEntity model = new ModelEntity();
    	model.setName(Model);
    	model.addTeacher(nuovo);
    	
    	modelservice.save(model);
    	nuovo.getModels().add(model);
    	this.m=model;
    	Response response = new Response("Done",model);
		return response;
		
	}
	
	@RequestMapping(value = "/NewPrecondition",  method = RequestMethod.POST)
	public Long NewPrecondition(@RequestBody ObjectNode node) throws JsonProcessingException, RestClientException, UnknownHostException{	
		RestTemplate restTemplate = new RestTemplate();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity nuovo = credentials.getUser();
    	ModelEntity model = this.modelservice.getModel(node.get("model_id").asLong());
    	RuleEntity rule = null;
             rule = new WikiRuleEntity();
             this.modelservice.saverule(rule);
             String name= node.get("rule_name").asText();
             name = name.replace(' ','_');
             System.out.println(name);
             Prova prova = restTemplate.getForObject("http://"+ InetAddress.getLocalHost().getHostAddress()+":5015/wiki?page="+name, Prova.class);
             ((WikiRuleEntity) rule).setUrl(prova.getUrl());
             System.out.println(prova.getPreconditions());
             rule.getTopics().addAll(prova.getCategories());
			 rule.setLength(prova.getLength());
			 List<RuleSuggestionRelationEntity> relations = new ArrayList<>();
			 int i = -1;
			 for (String pre : prova.getPreconditions()) {
				 i++;
				 RuleSuggestionRelationEntity relation = new RuleSuggestionRelationEntity();
			     WikiSuggestionEntity suggestion = null;
			     if (  modelservice.getpage(pre)== null) {
			    	 System.out.println("Ciao");
			    	 
			    	 suggestion = new WikiSuggestionEntity();
						suggestion.setPage(pre);
						modelservice.savewikisuggestion(suggestion);
				}else {
					 System.out.println("Ciao2");
					 
					suggestion = modelservice.getpage(pre);
				}    
			    
			     
			     relation.setRule(rule);
			     relation.setSuggestion(suggestion);
			     relation.setScore( Math.round((prova.getRank1().get(i).doubleValue())*100.0)/100.0);
			     relation.setScore2(Math.round((prova.getRank2().get(i).doubleValue())*100.0)/100.0);
			     relations.add(relation); 
			     System.out.println(relation.getSuggestion().getId());
			     relationservice.saverelation(relation);
			     rule.getSuggestions().add(relation);
			 }     
			
    	
			     RuleEntity main_rule= this.modelservice.getRule(node.get("rule_id").asLong());
			    	rule.addEffect(main_rule);
			    	main_rule.addPrecondition(rule);
			     rule.setName(node.get("rule_name").asText());
			    	this.modelservice.saverule(main_rule);
    	this.modelservice.saverule(rule);
    	model.getRules().add(rule);
    	modelservice.save(model);
    	
    	
    	
		Response response = new Response("Done");
		return rule.getId();
}
	
	@RequestMapping(value = "/Newrule",  method = RequestMethod.POST)
	public Long NewModel(@RequestBody ObjectNode node,@RequestBody MultipartFile uploadfile) throws RestClientException, IllegalStateException, IOException{	
		RestTemplate restTemplate = new RestTemplate();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity nuovo = credentials.getUser();
    	ModelEntity model = this.m;
    	RuleEntity rule = null;
    	System.out.println(node.get("model_name").asText());
    	switch(node.get("rule_type").asText()) {
    	 case "Testo":
             rule = new TextRuleEntity();
             ((TextRuleEntity) rule).setText(node.get("rule_text").asText());
             this.modelservice.saverule(rule);
             break;
         case "Pagina Web":
             rule = new WebRuleEntity();
             ((WebRuleEntity) rule).setUrl(node.get("rule_url").asText());
             this.modelservice.saverule(rule);
             break;
         case "Pagina Wikipedia":
             rule = new WikiRuleEntity();
             this.modelservice.saverule(rule);
             String name= node.get("rule_name").asText();
             name = name.replace(' ','_');
             Prova prova = restTemplate.getForObject("http://80.211.16.32:5015/wiki?page=" + name, Prova.class);
             ((WikiRuleEntity) rule).setUrl(prova.getUrl()); 
             rule.getTopics().addAll(prova.getCategories());
			 rule.setLength(prova.getLength());
			 List<RuleSuggestionRelationEntity> relations = new ArrayList<>();
			 int i = -1;
			 for (String pre : prova.getPreconditions()) { 
				 i++;
				 RuleSuggestionRelationEntity relation = new RuleSuggestionRelationEntity();
			     WikiSuggestionEntity suggestion = null;
			     if (  modelservice.getpage(pre)== null) {
			    	 System.out.println("Ciao");
			    	 
			    	 suggestion = new WikiSuggestionEntity();
						suggestion.setPage(pre);
						modelservice.savewikisuggestion(suggestion);
				}else {
					 
					suggestion = modelservice.getpage(pre);
				}    
			    
			     
			     relation.setRule(rule);
			     relation.setSuggestion(suggestion);
			    
			     relation.setScore( Math.round((prova.getRank1().get(i).doubleValue())*100.0)/100.0);
			     relation.setScore2(Math.round((prova.getRank2().get(i).doubleValue())*100.0)/100.0);
			     relations.add(relation); 
			     System.out.println(relation.getSuggestion().getId());
			     
			     relationservice.saverelation(relation);
			     rule.getSuggestions().add(relation);
			 }     
			    
			    
			  
			 break;     
         case "File":
        	 rule = new FileRuleEntity();
        	 String file1 =  uploadfile.getOriginalFilename();
         	System.out.println(file1);
         	String baseDir=System.getProperty("user.dir")+"\\FileRule";
         	uploadfile.transferTo(new File(baseDir + file1));
         	((FileRuleEntity) rule).setSrc(baseDir +file1);
         	this.modelservice.saverule(rule);
        	 break;
    	}
    /*	if(node.get("effect_id").asLong() == -1) {
    		final RuleEntity effect_entity = this.modelservice.getRule(node.get("effect_id").asLong());
    		if(effect_entity instanceof WikiRuleEntity ) {
    			Optional<RuleSuggestionRelationEntity> relation = effect_entity.getSuggestions().stream()
                        .filter(s -> ((WikiSuggestionEntity) s.getSuggestion()).getPage().equals(node.get("model_name").asText())).findAny();
    
    		}
    	}*/
    	rule.setName(node.get("model_name").asText());
    	this.modelservice.saverule(rule);
    	model.getRules().add(rule);
    	modelservice.save(model);
    	
		Response response = new Response("Done");
		return model.getId();
		
	}
	
	@RequestMapping(value = "/NewLesson",  method = RequestMethod.POST)
	public Response Newlesson(@RequestBody ObjectNode node) throws JsonMappingException, JsonProcessingException{	
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity nuovo = credentials.getUser();
    	LessonEntity lesson = new LessonEntity();
    
    	
    	lesson.setName(node.get("name").asText());
    	lesson.setModel(modelservice.getModel(node.get("models").asLong()));
    	
    	ObjectMapper mapper = new ObjectMapper();
    	long[] map = Starter.mapper.readValue(node.get("students").asText(), long[].class);
    	for(long id : map) {
    		UserEntity student = this.userservice.getUserId(Long.valueOf(id));
    		lesson.addStudent(student);
    		student.getFollowing_lessons().add(lesson);
    		this.userrepository.save(student);
    		System.out.println("User subscribe :  " + id);
    	}
    	
    	
    	lesson.setTeacher(nuovo);
    	//List<Integer> goals = mapper.readValue(node.get("goals").asText(), List.class);
    	List<Long> goals = mapper.readValue(node.get("goals").asText(), new TypeReference<List<Long>>(){});
    	for(Long g : goals) {
    		System.out.println("ID " + Long.valueOf(g));
    		lesson.getGoals().add(this.modelservice.getRule(Long.valueOf(g)));
    	}
    	
    	lessonservice.save(lesson);
    	nuovo.getTeaching_lessons().add(lesson);
    	userservice.saveUser(nuovo);
		Response response = new Response("Ciao",lesson);
		send.notify("prova", UserController.ONLINE.get(nuovo.getId()));
		 final LessonManager lesson_manager = new LessonManager(lesson,send,this.modelservice,this.userservice);
		LESSONS.put(lesson.getId(),lesson_manager);
		lesson_manager.Solve();
		for(UserEntity u : lesson.getFollowed_by()) {
			if(UserController.ONLINE.get(u.getId())!=null)
			send.notify(Starter.mapper.writeValueAsString(new Message.Subscribe(lesson.getId(), lesson.getName())), UserController.ONLINE.get(u.getId()));
		}
		return response;
		
	}
	
	@PostMapping("/iscrizione")
	public Response iscrizione(@RequestBody Long id){
		Response response = new Response("Done");
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity nuovo = credentials.getUser();
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
	public List<Model> getAllModel(){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity nuovo = credentials.getUser();
    	List<Model> m = this.modelrepository.findByTeachersonly(nuovo.getId());
		return m;
		
	}
	
	/*@PostMapping("/Argomento/Getlessonsreal")
	public List<LessonEntity> getAlllessons(@RequestBody ObjectNode node){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
    	List<ModelEntity> m = modelservice.getModelTeacher(nuovo);
    	System.out.println("User subscribe :  " + node.get("id").asLong());
		return this.modelservice.getModel(node.get("id").asLong()).;
		
	}*/
	
	@GetMapping("/Getlessons_student")
	public Response Getlessons_student(){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity nuovo = credentials.getUser();
    	List<LessonEntity> l = nuovo.getFollowing_lessons();
		Response response = new Response("Done");		
		for(Long u : UserController.ONLINE.keySet()) {
			System.out.println("ID: "+u);
			send.notify("prova", UserController.ONLINE.get(u));
		}
		return response;
		
	}
	
	@PostMapping("/provamessaggio")
	public String Prova43() {
		for(Long u : UserController.ONLINE.keySet()) {
			System.out.println("ID: "+u);
			send.notify("prova", UserController.ONLINE.get(u));
		}
		return"funge";
	}
	 public static MessageHeaders createHeaders(String sessionId) {
			SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
			headerAccessor.setSessionId(sessionId);
			headerAccessor.setLeaveMutable(true);
			return headerAccessor.getMessageHeaders();
		}
	
	@GetMapping("/getstudents")
	public List<UserEntity> Getstudents(){
				
		return userservice.getTeacher("STUDENT");
		
	}
	
	@GetMapping("lezione/lessons")
	public Response getlessons2(){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity nuovo = credentials.getUser();
		LessonEntity n= new LessonEntity();
		n.setName("pippo");
		List<LessonEntity> l = new ArrayList<LessonEntity>();
		l.addAll(lessonservice.getlesson(nuovo));

		
		Response response = new Response("Done",l);			
		return response;
		
	}
	
	@PostMapping("/getname")
	public Response getName(@RequestBody ObjectNode node) {
		
		Response response = new Response(this.modelservice.getRuleName(node.get("id").asLong()));	
		return response;
		
	}
	
	
	@GetMapping("/lesson")
	public Response getlesson(){	
		Response response = new Response("Done",this.m);	
		return response;
		
	}
	
	@PostMapping("/getEmail")
	public Response email(@RequestBody ObjectNode node){
		Response response = new Response("Done");
		System.out.println("prova");
		if(credentialservice.getUser(node.get("email").asText()) == null) {
			response.setStatus("Done");
		}else {
			response.setStatus("No");
		}
		
		return response;
		
	}
	
	
	
	
}
