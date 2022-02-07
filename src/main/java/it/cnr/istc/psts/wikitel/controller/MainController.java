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
import it.cnr.istc.psts.wikitel.db.FileEntity;
import it.cnr.istc.psts.wikitel.db.LessonEntity;
import it.cnr.istc.psts.wikitel.db.Model;
import it.cnr.istc.psts.wikitel.db.ModelEntity;
import it.cnr.istc.psts.wikitel.db.Prova;
import it.cnr.istc.psts.wikitel.db.RuleEntity;

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



import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import it.cnr.istc.psts.wikitel.db.TextRuleEntity;
import it.cnr.istc.psts.wikitel.db.User;
import it.cnr.istc.psts.wikitel.db.WebRuleEntity;
import it.cnr.istc.psts.wikitel.db.WikiRuleEntity;
import it.cnr.istc.psts.wikitel.db.WikiSuggestionEntity;
import it.cnr.istc.psts.wikitel.db.RuleSuggestionRelationEntity;
import it.cnr.istc.psts.Websocket.Sending;
import it.cnr.istc.psts.wikitel.Repository.ModelRepository;
import it.cnr.istc.psts.wikitel.Repository.Response;

import it.cnr.istc.psts.wikitel.Repository.UserRepository;

import it.cnr.istc.psts.wikitel.Service.FileService;
import it.cnr.istc.psts.wikitel.Service.LessonService;
import it.cnr.istc.psts.wikitel.Service.ModelService;
import it.cnr.istc.psts.wikitel.Service.RuleSuggestionRelationService;
import it.cnr.istc.psts.wikitel.Service.UserService;
import it.cnr.istc.psts.wikitel.db.UserEntity;

@RestController
public class MainController {
	
	static final Logger LOG = LoggerFactory.getLogger(MainController.class);
	
	  @Autowired
	    protected PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userrepository;
	
	@Autowired
	private UserService userservice;
		
	@Autowired
	private LessonService lessonservice;
	
	
	@Autowired
	private FileService fileservice;
	
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
	
	private UserEntity current_user;
	
	static final Map<Long, LessonManager> LESSONS = new HashMap<>();
	public  List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
	
	@PostMapping("/register")
	public Response register(@RequestBody  ObjectNode node) throws JsonGenerationException, JsonMappingException, IOException{
		System.out.println("entratoodòjfkòjòfdaeHJLFJ");
		Json_reader interests=new Json_reader();
		Response response = new Response("Done");
		UserEntity nuovo = new UserEntity();
		nuovo.setEmail(node.get("email").asText());
		nuovo.setFirst_name(node.get("first_name").asText());
		nuovo.setLast_name(node.get("last_name").asText());
		nuovo.setPassword(this.passwordEncoder.encode(node.get("password").asText()));
		nuovo.setProfile(node.get("profile").asText());
		nuovo.setSrc(node.get("src").asText());
		nuovo.setRole(node.get("role").asText());
		nuovo.setQuestionario(node.get("one").asText());
		current_user = nuovo;
		userrepository.save(nuovo);
		System.out.println("Done");
		String eventFormatted = new JSONObject()
        .put ("title", "prova")
        .put ("text", "pippo").toString();
		for ( SseEmitter emitter : emitters) {
		    try {
		        emitter.send(SseEmitter.event().name("latestNews").data(eventFormatted));
		     } catch (IOException e) {
		        emitters.remove(emitter);
		     }
		}
		return response;
		
	}
	@MessageMapping("/register")
	public void prova(@Payload pippo pippo, SimpMessageHeaderAccessor headerAccessor ) {
		UserController.ONLINE.put(pippo.getUser_id(), pippo.getSession());
		System.out.println("sessionID" + pippo.getSession());
	}
	 
	
	
	
	@GetMapping("/getprofile")
	public Response getProfile() throws JsonMappingException, JsonProcessingException {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
		Response response = new Response( nuovo.getProfile());
		ObjectMapper mapper = new ObjectMapper();
		Json_reader interests = pageController.json("/json/user_model.json",true);
    	System.out.println("ciao " +interests.getInterests().get(0).getId());
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
    	System.out.println("username2:PIPPO");
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
		nuovo.setEmail(user.getEmail());
		nuovo.setFirst_name(user.getFirst_name());
		nuovo.setLast_name(user.getLast_name());
		userrepository.save(nuovo);
		
		System.out.println(nuovo.getPassword());
		
		return response;
		
	}
	
	@PostMapping("/edit_interests")
	public Response edit_interests(@RequestBody User user) throws JsonGenerationException, JsonMappingException, IOException{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
    	nuovo.setProfile(user.getProfile());
    	userservice.saveUser(nuovo);
    	Response response = new Response("Done", nuovo);
    	ObjectMapper mapper = new ObjectMapper();
    	List<String> profile = mapper.readValue(nuovo.getProfile(), new TypeReference<List<String>>(){});
    	System.out.println("ciao " +profile.get(0));
		return response;
		
	}
	
	@PostMapping("/uploadFile_profile")
	public Response uploadfileprofile(@RequestBody MultipartFile uploadfile ) throws IllegalStateException, IOException{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String file1 =  uploadfile.getOriginalFilename();
    	System.out.println(file1);
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
    	System.out.println(nuovo.getFirst_name());
    	String baseDir=System.getProperty("user.dir")+"\\src\\main\\resources\\static\\images\\";
    	uploadfile.transferTo(new File(baseDir + nuovo.getId() +".jpg"));
    	nuovo.setSrc("\\images\\" + nuovo.getId() + ".jpg");
    	this.userservice.saveUser(nuovo);
    	Response response = new Response("Done");
		return response;
		
	}
	
	@PostMapping("/uploadFileText_profile")
	public Response uploadfiletextprofile(@RequestBody MultipartFile uploadfile ) throws IllegalStateException, IOException{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
    	nuovo.getFile().add(fileservice.save(uploadfile));
    	System.out.println(uploadfile);
    	Response response = new Response("Done");
		return response;
		
	}
	
	@PostMapping("/uploadFile")
	public Response uploadfile(@RequestBody MultipartFile uploadfile ) throws IllegalStateException, IOException{

		String file1 =  uploadfile.getOriginalFilename();
    	System.out.println(file1);
    	System.out.println(current_user.getFirst_name());
    	String baseDir=System.getProperty("user.dir")+"\\src\\main\\resources\\static\\images\\";
    	uploadfile.transferTo(new File(baseDir + current_user.getId() +".jpg"));
    	current_user.setSrc("\\images\\" + current_user.getId() + ".jpg");
    	this.userservice.saveUser(current_user);
    	Response response = new Response("Done");
		return response;
		
	}
	
	@PostMapping("/uploadFileText")
	public ModelEntity uploadfiletext(@RequestBody MultipartFile uploadfile ) throws IllegalStateException, IOException{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
    	nuovo.getFile().add(fileservice.save(uploadfile));
    	System.out.println(uploadfile);

		return this.m;
		
	}
	
	@GetMapping("/downloadFile/{fileid}")
	public ResponseEntity<ByteArrayResource> getmodel(@PathVariable Long fileid){
		System.out.println(fileid);
		FileEntity file = this.fileservice.getfile(fileid);
		System.out.println(file.getName());
		return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(file.getType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
        .body(new ByteArrayResource(file.getContent()));
		
	}
	
	@PostMapping("/uploadFileString")
	public Response uploadFileString(@RequestBody ObjectNode node ) throws IllegalStateException, IOException{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
    	System.out.println(node.get("src").asText());
    	nuovo.setSrc(node.get("src").asText());
    	this.userservice.saveUser(nuovo);
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
    	System.out.println("username2:PIPPO");
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
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
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
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
	public Long NewModel(@RequestBody ObjectNode node) throws JsonProcessingException, RestClientException, UnknownHostException{	
		RestTemplate restTemplate = new RestTemplate();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
    	ModelEntity model = this.m;
    	RuleEntity rule = null;
    	System.out.println(node.get("model_name").asText());
    	switch(node.get("rule_type").asText()) {
    	 case "Testo":
             rule = new TextRuleEntity();
             ((TextRuleEntity) rule).setText(node.get("rule_text").asText());
            
             break;
         case "Pagina Web":
             rule = new WebRuleEntity();
             ((WebRuleEntity) rule).setUrl(node.get("rule_url").asText());
             break;
         case "Pagina Wikipedia":
             rule = new WikiRuleEntity();
             this.modelservice.saverule(rule);
             
             Prova prova = restTemplate.getForObject("http://" + InetAddress.getLocalHost().getHostAddress() + ":5015/wiki?page="+ node.get("model_name").asText(), Prova.class);
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
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
    	LessonEntity lesson = new LessonEntity();
    
    	
    	lesson.setName(node.get("name").asText());
    	lesson.setModel(modelservice.getModel(node.get("models").asLong()));
    	
    	ObjectMapper mapper = new ObjectMapper();
    	List<Integer> map = mapper.readValue(node.get("students").asText(), List.class);
    	for(int id : map) {
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
		 final LessonManager lesson_manager = new LessonManager(lesson,send);
		LESSONS.put(lesson.getId(),lesson_manager);
		lesson_manager.Solve();
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
	public List<Model> getAllModel(){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
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
    	UserEntity nuovo =  userservice.getUser(userDetails.getUsername());
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
		Response response = new Response("Done",this.m);	
		return response;
		
	}
	
	@PostMapping("/getEmail")
	public Response email(@RequestBody ObjectNode node){
		Response response = new Response("Done");
		System.out.println("prova");
		if(userservice.getUser(node.get("email").asText()) == null) {
			response.setStatus("Done");
		}else {
			response.setStatus("No");
		}
		
		return response;
		
	}
	
	
	
	
}
