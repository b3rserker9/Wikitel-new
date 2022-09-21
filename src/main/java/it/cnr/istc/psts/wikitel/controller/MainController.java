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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.netty.handler.codec.MessageAggregationException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import it.cnr.istc.psts.Websocket.Sending;
import it.cnr.istc.psts.wikitel.MongoRepository.RuleMongoRepository;

import it.cnr.istc.psts.wikitel.Mongodb.RuleMongo;
import it.cnr.istc.psts.wikitel.Mongodb.SuggestionM;
import it.cnr.istc.psts.wikitel.Mongodb.SuggestionMongo;
import it.cnr.istc.psts.wikitel.Repository.ModelRepository;

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
	private RuleMongoRepository rulemongorep;
	
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
	
	private Long Fileid;
	
	  @Autowired
	    private ObjectMapper objectMapper;
	
	
	public static final Map<String, LessonManager> LESSONS = new HashMap<>();
	
	
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
			System.out.println(this.credentialservice.getCredentialsUser(session.getUser_id()).getRole());
			if(this.credentialservice.getCredentialsUser(session.getUser_id()).getRole().equals("TEACHER")) {
				for(UserEntity u : this.lessonservice.lezionePerId(session.getLesson_id()).getFollowed_by()) {
					String n = String.valueOf(session.getLesson_id()) + String.valueOf(u.getId());
					LessonManager manager = MainController.LESSONS.get(n);
					send.notify(Starter.mapper.writeValueAsString(new Message.User(u.getId())), session.getSession());
					send.notify(Starter.mapper.writeValueAsString( new LessonManager.Timelines(session.getLesson_id(), manager.getSolver().getTimelines())), session.getSession());
					send.notify(Starter.mapper.writeValueAsString(new LessonManager.Tick(session.getLesson_id(), manager.getCurrentTime())), session.getSession());
				}
			}else {
				
				String n = String.valueOf(session.getLesson_id()) + String.valueOf(session.getUser_id());
		LessonManager manager = MainController.LESSONS.get(n);
		
		send.notify(Starter.mapper.writeValueAsString( new LessonManager.Timelines(session.getLesson_id(), manager.getSolver().getTimelines())), session.getSession());
		send.notify(Starter.mapper.writeValueAsString(new LessonManager.Tick(session.getLesson_id(), manager.getCurrentTime())), session.getSession());
			
		if(manager.st!=null) {
		send.notify(Starter.mapper.writeValueAsString(MainController.LESSONS.get(n).st), UserController.ONLINE.get(session.getUser_id()));	
		}
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
	public Set<RuleEntity> GetSuggestion(@RequestBody ObjectNode node) {
		System.out.println(node.get("ids").asLong());
		ModelEntity model = this.modelservice.getModel(node.get("ids").asLong());
		
		Set<RuleEntity> re = new HashSet<>();
		for(RuleEntity r : model.getRules()) {
			RuleEntity rule = new RuleEntity();
				rule.setLength(r.getLength());
				rule.setId(r.getId());
				rule.setName(r.getName()); 
				if(r.getSuggestions()!= null)
					rule.getSuggestionm().addAll(this.modelservice.getsuggestion(r.getSuggestions()).getSuggestion());
				re.add(rule);
			
				
		}
		return re;
	}
	
	@PostMapping("/findsuggestiontot")
	public List<Set<ObjectNode>> GetSuggestiontot(@RequestBody ObjectNode node) {
		System.out.println(node.get("ids").asLong());
		ModelEntity model = this.modelservice.getModel(node.get("ids").asLong());
		
		Set<RuleEntity> re = new HashSet<>();
		Set<ObjectNode> nodes = new HashSet<>();
		Set<ObjectNode> edges = new HashSet<>();
		Set<ObjectNode> nodes2 = new HashSet<>();
		Set<ObjectNode> nodes50 = new HashSet<>();
		Set<ObjectNode> rules = new HashSet<>();
		
		
		long i=0 ;
		for(RuleEntity rule : model.getRules()) {
			ObjectNode  robgr= objectMapper.createObjectNode();   
			robgr.put("id",rule.getName()); 
			robgr.put("label",rule.getName()); 
			robgr.put("group", i);
			robgr.put("type", "rule");
			robgr.put("shape", "triangle");
			if (rule instanceof WikiRuleEntity)
			robgr.put("rule_type", "wiki");
			else if (rule instanceof TextRuleEntity) {
				robgr.put("rule_type", "text");
				robgr.put("rule_text", this.modelservice.getText(rule.getId()));
			}
			else if (rule instanceof WebRuleEntity)
				robgr.put("rule_type", "web");
			else if (rule instanceof FileRuleEntity) {
				robgr.put("rule_type", "file");
				
			}
			robgr.put("rule_id", rule.getId());
			nodes.add(robgr);
			nodes50.add(robgr);
			rules.add(robgr);
			i++;
		}
		i=0;
		for(RuleEntity r : model.getRules()) {
			RuleEntity rule = new RuleEntity();
				rule.setLength(r.getLength());
				rule.setId(r.getId());
				rule.setName(r.getName()); 
				if(r.getSuggestions()!= null)
				rule.getSuggestionm().addAll(this.modelservice.getsuggestion(r.getSuggestions()).getSuggestion());
				re.add(rule);
				List<Double> max = new ArrayList<>();
				for(SuggestionMongo sm : rule.getSuggestionm()) {
					max.add(sm.getScore2());
				}
				if(r.getSuggestions()!= null) {
			for(SuggestionMongo sm : rule.getSuggestionm()) {
				if(!containsName(nodes, sm.getPage())) {
					
				ObjectNode obj=objectMapper.createObjectNode();    
				obj.put("id",sm.getPage()); 
				obj.put("label",sm.getPage()); 
				obj.put("group", i);
				obj.put("type", "sug");
				obj.put("score", sm.getScore2());
				obj.put("max", Collections.max(max));
				obj.put("rule_id", rule.getId());
				nodes.add(obj);
				ObjectNode obj2=objectMapper.createObjectNode(); 
				obj2.put("id",sm.getPage());
				obj2.put("parent",rule.getName());
				obj2.put("rule_id", rule.getId());
				obj2.put("score1",sm.getScore());
				obj2.put("score2",sm.getScore2());
				obj2.put("type", "Suggestion");
				obj2.put("label",sm.getPage()); 
				obj2.put("group", i);
				nodes2.add(obj2);
				ObjectNode obj3=objectMapper.createObjectNode();
				if( 70 * Collections.max(max) / 100 <= sm.getScore2()) {
				obj3.put("id",sm.getPage()); 
				obj3.put("label",sm.getPage()); 
				obj3.put("group", i);
				obj3.put("type", "sug");
				obj3.put("score", sm.getScore2());
				nodes50.add(obj2);
				}
				}
				ObjectNode edge=objectMapper.createObjectNode();
				edge.put("from",rule.getName());    
				edge.put("to",sm.getPage()); 
				edges.add(edge);
			}
		}
			for(RuleEntity effect : r.getPreconditions()) {
				ObjectNode edge=objectMapper.createObjectNode();
				edge.put("from",rule.getName());    
				edge.put("to",effect.getName()); 
				edges.add(edge);
			}
			i++;
				
		}
		
		List<Set<ObjectNode>> tot = new ArrayList<>();
		tot.add(0,edges);
		tot.add(1,nodes);
		tot.add(2,nodes2);
		tot.add(3,rules);
		tot.add(4,nodes50);
		return tot;
	}
	
	
	public boolean containsName(final Set<ObjectNode> list, final String name){
	    return list.stream().filter(o -> o.get("id").asText().equals(name)).findFirst().isPresent();
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
	
	
	@PostMapping("/ciao")
	public String edit4(){
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
     	rule.setLength((long)0);
     	this.modelservice.saverule(rule);
     	this.m.addRule(rule);
     	this.modelservice.save(m);
    	
		return m.getId();
		
	}
	
	@PostMapping("/uploadFileRulePre")
	public Long uploadfilerulepre(@RequestBody MultipartFile uploadfile ) throws IllegalStateException, IOException{
		
		String file1 =  uploadfile.getOriginalFilename();
    	System.out.println(file1);
    	String baseDir=System.getProperty("user.dir")+"\\FileRule\\";
    	System.out.println(baseDir);
    	uploadfile.transferTo(new File(baseDir + file1));
    	RuleEntity rule = new FileRuleEntity();   	
     	((FileRuleEntity) rule).setSrc(baseDir +file1);
     	this.modelservice.saverule(rule);
     	Fileid = rule.getId();
		return rule.getId();
		
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
	
	@RequestMapping("/fileRule/{id}")
	  public ResponseEntity<InputStreamResource> downloadFile1Rule(
			  @PathVariable("id") Long id) throws IOException {

			
	        File file = new File(this.modelservice.getFile(id));
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
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity nuovo = credentials.getUser();
			
	        File file = new File(System.getProperty("user.dir")+"//riddle//" + id + nuovo.getId() + ".rddl");
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
		
		for(UserEntity u : this.lessonservice.lezionePerId(node.get("id").asLong()).getFollowed_by()) {
		String n = String.valueOf(node.get("id").asLong()) + String.valueOf(u.getId());
		System.out.println(n);
		System.out.println(LESSONS);
    	LESSONS.get(n).play();
		}
    	Response response = new Response("Done");
		return response;
		
	}
	@PostMapping("/pause")
	public Response pauseLesson(@RequestBody ObjectNode node ) throws IllegalStateException, IOException{
		for(UserEntity u : this.lessonservice.lezionePerId(node.get("id").asLong()).getFollowed_by()) {
			String n = String.valueOf(node.get("id").asLong()) + String.valueOf(u.getId());
			System.out.println(n);
			System.out.println(LESSONS);
	    	LESSONS.get(n).pause();
			}
   
  
    	Response response = new Response("Done");
		return response;
		
	}
	
	@PostMapping("/stop")
	public Response stopLesson(@RequestBody ObjectNode node ) throws IllegalStateException, IOException{
		for(UserEntity u : this.lessonservice.lezionePerId(node.get("id").asLong()).getFollowed_by()) {
			String n = String.valueOf(node.get("id").asLong()) + String.valueOf(u.getId());
			System.out.println(n);
			System.out.println(LESSONS);
	    	LESSONS.get(n).stop();
			}

    
    	Response response = new Response("Done");
		return response;
		
	}
	
	
	
	@PostMapping("/Argomento/Getmodel")
	public Collection<RuleEntity> getmodel(@RequestBody ObjectNode node){
		ModelEntity model =  this.modelservice.getModel(node.get("ids").asLong());
		return model.getRules();
		
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
	
	
	@RequestMapping(value = "/Newrule",  method = RequestMethod.POST)
	public Response NewModel(@RequestBody ObjectNode node,@RequestBody MultipartFile uploadfile) throws Exception{	
		RestTemplate restTemplate = new RestTemplate();
		boolean bool = true;
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity nuovo = credentials.getUser();
		
		ModelEntity model = this.m;
		final String  name= node.get("rule_name").asText();
		JsonNode effect= node.get("rule_id");
    	if(effect!=null)
    		model = this.modelservice.getModel(node.get("model_id").asLong());
		RuleMongo rulemongo = new RuleMongo();
    	RuleEntity rule = null;
    	
    	send.notify(Starter.mapper.writeValueAsString(new Message.Searching(name, 0)), UserController.ONLINE.get(nuovo.getId()));
    	switch(node.get("rule_type").asText()) {
    	 case "Testo":
             rule = new TextRuleEntity();
             ((TextRuleEntity) rule).setText(node.get("rule_text").asText());
             rule.setLength(node.get("rule_length").asLong());
             this.modelservice.saverule(rule);
             break;
         case "Pagina Web":
             rule = new WebRuleEntity();
             ((WebRuleEntity) rule).setUrl(node.get("rule_text").asText());
             rule.setLength(node.get("rule_length").asLong());
             this.modelservice.saverule(rule);
             break;
         case "Pagina Wikipedia":
        	 rule = new WikiRuleEntity();
        	 SuggestionM sm = new SuggestionM();
        	 if(this.modelservice.getrulemongoname(name) == null) {
             
             Prova prova = restTemplate.getForObject("http://80.211.16.32:5015/wiki?page=" + name.replace(' ','_'), Prova.class);
             if(prova.getExists()) {
             ((WikiRuleEntity) rule).setUrl(prova.getUrl());
			 List<RuleSuggestionRelationEntity> relations = new ArrayList<>();
			 
			 int i = -1;
			 for (String pre : prova.getPreconditions()) { 
				 i++;
				 SuggestionMongo sugmongo = new SuggestionMongo();
			//	 RuleSuggestionRelationEntity relation = new RuleSuggestionRelationEntity();
					
			   //  WikiSuggestionEntity suggestion = null;
		
			     
			    	// suggestion = new WikiSuggestionEntity();
					//	suggestion.setPage(pre);
						sugmongo.setPage(pre);
						
 
			    
			     
			  //   relation.setRule(rule);
			   //  relation.setSuggestion(suggestion);
			    
			   //  relation.setScore( Math.round((prova.getRank1().get(i).doubleValue())*100.0)/100.0);
			   //  relation.setScore2(Math.round((prova.getRank2().get(i).doubleValue())*100.0)/100.0);
			     sugmongo.setScore( Math.round((prova.getRank1().get(i).doubleValue())*100.0)/100.0);
			     sugmongo.setScore2(Math.round((prova.getRank2().get(i).doubleValue())*100.0)/100.0);
			   //  relations.add(relation); 
			   
			    
			    // relationservice.saverelation(relation);
			     rulemongo.getSuggestions().add(sugmongo);
			     sm.getSuggestion().add(sugmongo);
			     
			   //  rule.getSuggestions().add(relation);
			     
			 }
			 this.modelservice.savesm(sm);
			 rule.setSuggestion(sm.getId());
			 rule.setLength(prova.getLength());
			 rule.getTopics().addAll(prova.getCategories());
			 rulemongo.setLength(prova.getLength());
		     rulemongo.getTopics().addAll(prova.getCategories());
        	 }
             else {
            	 System.out.println("ELEMENTO NON TROVATO PROVA CON " + prova.getSuggest() + " " + prova.getMaybe());
            	 Response response = new Response(prova.getExists(),prova.getSuggest(),prova.getMaybe());
            	 return response;
             }
			 }     
        	 else {
        		 bool=false;
        		 System.out.println("non sono entrato");
        		 List<RuleSuggestionRelationEntity> relations = new ArrayList<>();
        		 RuleMongo m = this.modelservice.getrulemongoname(name);
        		 rule.setLength(m.getLength());
        		 rule.setName(name);
        		 for (SuggestionMongo s : m.getSuggestions()) {
        			 sm.getSuggestion().add(s);
        			 //RuleSuggestionRelationEntity relation = new RuleSuggestionRelationEntity();
        			 //WikiSuggestionEntity suggestion = new WikiSuggestionEntity();
        			 //suggestion.setPage(s.getPage());
        			 //modelservice.savewikisuggestion(suggestion);
        			 //relation.setRule(rule);
        			 //relation.setSuggestion(suggestion);
        			 //relation.setScore(s.getScore());
        			 //relation.setScore2(s.getScore2());
        			 //relations.add(relation);
        			 //relationservice.saverelation(relation);
        			 //rule.getSuggestions().add(relation);
        			 
        			 
        		 }
        		 
        		 this.modelservice.savesm(sm);
    			 rule.setSuggestion(sm.getId());
    			 rule.getTopics().addAll(m.getTopics());
    			 ((WikiRuleEntity) rule).setUrl("https://it.wikipedia.org/wiki/"+ name );
        		 
        	 }
			    
			    
			  
			 break;     
         case "File":
        	 rule = this.modelservice.getRule(Fileid);
        	 rule.setName(name);
        	 rule.setLength(node.get("rule_length").asLong());
             this.modelservice.saverule(rule);
        	 break;
    	}
    	if (effect != null) {
            final RuleEntity effect_entity = this.modelservice.getRule(effect.asLong());
            if (effect_entity == null)
                throw new Exception();
            rule.addEffect(effect_entity);
            effect_entity.addPrecondition(rule);

          if (effect_entity instanceof WikiRuleEntity) {
        	  System.out.println("sono qui!!");
        	  SuggestionM sm = this.modelservice.getsuggestion(effect_entity.getSuggestions());
                Optional<SuggestionMongo> relation = sm.getSuggestion().stream()
                        .filter(s -> s.getPage().equals(name)).findAny();
                relation.ifPresent(rel -> {
                	System.out.println(rel);
                    sm.getSuggestion().remove(rel);
                    this.modelservice.savesm(sm);
                });
            }
          this.modelservice.saverule(effect_entity);
        }
    	if(bool) {
    	rule.setName(node.get("rule_name").asText());
    	rulemongo.setTitle(name);;
    	rulemongorep.save(rulemongo);
		
    	}
    	this.modelservice.saverule(rule);
    	model.getRules().add(rule);
    	modelservice.save(model);
    	send.notify(Starter.mapper.writeValueAsString(new Message.Searching(name, 1)), UserController.ONLINE.get(nuovo.getId()));
    	Response response = new Response(true,model.getId());
		return response;
		
	}
	
	@RequestMapping(value =  "/deletemodel/{id}" , method = RequestMethod.POST)
	 public String deletemodel(@PathVariable("id") Long id) {
		 UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
			UserEntity userentity = credentials.getUser();
			this.modelservice.delete(id, userentity);
			System.out.println("OKK");
			return "OK";
	 }
	
	@RequestMapping(value =  "/deletelesson/{id}" , method = RequestMethod.POST)
	 public String deletelesson(@PathVariable("id") Long id) {
		 UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
			UserEntity userentity = credentials.getUser();
			this.lessonservice.delete(this.lessonservice.lezionePerId(id));
			System.out.println("OKK");
			return "OK";
	 }
	
	@RequestMapping(value =  "/getrulecat/{id}" , method = RequestMethod.GET)
	 public Collection<RuleEntity> getrulecat(@PathVariable("id") Long id) {

			
			return this.lessonservice.lezionePerId(id).getGoals();
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
		
		System.out.println(UserController.ONLINE);
	
		
		long[] map = Starter.mapper.readValue(node.get("students").asText(), long[].class);
		List<UserEntity> u = new ArrayList<>();
    	for(long id : map) {
    		UserEntity student = this.userservice.getUserId(Long.valueOf(id));
    		u.add(student);
    		lesson.addStudent(student);
    		student.getFollowing_lessons().add(lesson);
    		this.userrepository.save(student);
    		System.out.println("User subscribe :  " + id);
    		String n = String.valueOf(lesson.getId()) + String.valueOf(Long.valueOf(id));
    		 final LessonManager lesson_manager = new LessonManager(lesson,send,this.modelservice,this.userservice);
    		 System.out.println("NUMERO" + n);
    		LESSONS.put(n,lesson_manager);
    		lesson_manager.Solve();
    		lesson.getFollowed_by().clear();
    	}
    	lesson.getFollowed_by().addAll(u);
    	lessonservice.save(lesson);
    	
    	for(UserEntity user : lesson.getFollowed_by()) {
			System.out.println("1");
			if(UserController.ONLINE.get(user.getId())!=null)
				System.out.println("2");
			send.notify(Starter.mapper.writeValueAsString(new Message.Lesson(lesson.getId(), lesson.getName(), lesson.getTeacher().getFirst_name()+ " " + lesson.getTeacher().getLast_name(),lesson.getTeacher().getId())), UserController.ONLINE.get(user.getId()));
			send.notify(Starter.mapper.writeValueAsString(new Message.Subscribe(lesson.getId(), lesson.getName())), UserController.ONLINE.get(user.getId()));
			System.out.println("3");
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
	
	@GetMapping("/provamessaggio")
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
	
	@GetMapping("/json")
	public RuleMongo Getstudentsds(){
				
		return this.modelservice.getrulemongoname("piramide");
		
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
