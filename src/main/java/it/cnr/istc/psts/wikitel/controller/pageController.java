package it.cnr.istc.psts.wikitel.controller;


import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.client.RestTemplate;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.cnr.istc.pst.oratio.Item.ArithItem;
import it.cnr.istc.pst.oratio.Solver;
import it.cnr.istc.pst.oratio.SolverException;
import it.cnr.istc.psts.WikitelNewApplication;
import it.cnr.istc.psts.Websocket.Sending;
import it.cnr.istc.psts.wikitel.Authentication.AuthConfiguration;
import it.cnr.istc.psts.wikitel.MongoRepository.RuleMongoRepository;
import it.cnr.istc.psts.wikitel.Mongodb.RuleMongo;
import it.cnr.istc.psts.wikitel.Repository.ModelRepository;
import it.cnr.istc.psts.wikitel.Service.CredentialService;
import it.cnr.istc.psts.wikitel.Service.LessonService;
import it.cnr.istc.psts.wikitel.Service.ModelService;
import it.cnr.istc.psts.wikitel.Service.Starter;
import it.cnr.istc.psts.wikitel.Service.UserService;
import it.cnr.istc.psts.wikitel.db.Credentials;
import it.cnr.istc.psts.wikitel.db.LessonEntity;
import it.cnr.istc.psts.wikitel.db.ModelEntity;
import it.cnr.istc.psts.wikitel.db.Prova;

import it.cnr.istc.psts.wikitel.db.User;
import it.cnr.istc.psts.wikitel.db.UserEntity;
import static it.cnr.istc.psts.wikitel.db.UserEntity.STUDENT_ROLE;
import it.cnr.istc.psts.wikitel.controller.*;





@Controller
public class pageController {
	

	
	@Autowired
	private UserService userservice;
	
	@Autowired
	private CredentialService credentialservice;
	
	
	@Autowired
	private RuleMongoRepository rulemongorep;
	
	@Autowired
	private LessonService lessonservice;
	
	@Autowired
	private ModelRepository modelrepository;
	
	@Autowired
	private Sending send;
	
	@Autowired
	private ModelService modelservice;
	 private final Solver s = new Solver();
	 
	 
	 



	@RequestMapping(value = {"/","/index"}, method = RequestMethod.GET)
	public String index(Model model) throws NoSuchFieldException, SolverException, IOException, InterruptedException {
		RestTemplate restTemplate = new RestTemplate();
		//Prova prova = restTemplate.getForObject("http://192.168.1.79:5015/wiki?page=Palombaro_lungo", Prova.class);
		//System.out.println(prova.getLength());
		Json_reader interests = json("/json/user_model.json",true);
		model.addAttribute("interests", interests.getInterests());
		System.out.println("mongo funziona??");
		
		System.out.println("ONLINE: "+UserController.ONLINE);
		Process process = Runtime.getRuntime().exec("python3 -c 'import C:\\Users\\aliyo\\OneDrive\\Desktop\\python.py;python.prova() '");
		process.waitFor();
		int exitCode = process.exitValue();
		System.out.println(exitCode);
		
			return "index";
	}
	
	 @GetMapping("/verify")
	  public String verify(@Param("code") String code,Model model) {
		 System.out.println(code);
		  if (this.credentialservice.verify(code)) {
			  model.addAttribute("complete", true);   
		    } else {
		    	model.addAttribute("complete", false);
		    }
		  return "registercode";
		  
	  }
	
	@RequestMapping(value = "/failure", method = RequestMethod.GET)
	public String failure(Model model) {
		//Json_reader interests = json("/json/user_model.json",true);
		System.out.println("CISONO");
		//model.addAttribute("interests", interests.getInterests());
		model.addAttribute("loginError", true);
			return "redirect:/index";
	}
	
	@RequestMapping(value = "/default", method = RequestMethod.GET)
    public String defaultAfterLogin(Model model) {
		Json_reader interests = json("/json/user_model.json",true);
		System.out.println("username1:PIPPO");
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity userentity = credentials.getUser();
    	model.addAttribute("model", this.modelrepository.findByTeachersonly(userentity.getId()));
    	model.addAttribute("interests", interests.getInterests());
    	model.addAttribute("user",userentity);
    	model.addAttribute("Teachers",userservice.getTeacher(userentity.TEACHER_ROLE));
    	if( MainController.newUsers.contains(userentity.getId()) ) {
    		model.addAttribute("first",true);
    	}
    	MainController.newUsers.remove(userentity.getId());
    	if(credentials.isEnabled()) {
    	if (credentials.getRole().equals(UserEntity.STUDENT_ROLE)) {
    		HashMap<String,ArrayList<LessonEntity>> t = new HashMap<>();
    		for(LessonEntity l : userentity.getFollowing_lessons()) {
    			if(t.containsKey(l.getTeacher().getFirst_name() + " " + l.getTeacher().getLast_name() )) {
    				t.get(l.getTeacher().getFirst_name() + " " + l.getTeacher().getLast_name()).add(l);
    			}else {
    				t.put(l.getTeacher().getFirst_name() + " " + l.getTeacher().getLast_name(), new ArrayList<>());
    				t.get(l.getTeacher().getFirst_name() + " " + l.getTeacher().getLast_name()).add(l);
    			}
    		}
    		System.out.println(t);
    		model.addAttribute("lessons", t);
            return "admin/hello";
        }
    	
    	//se non lo e'
        return "teachers/index";
    	}
    	model.addAttribute("complete",false);
    	return "registercode";
    }
	
	@RequestMapping(value =  "/deletemodel/{id}" , method = RequestMethod.GET)
	 public String deletemodel(@PathVariable("id") Long id) {
		 UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
			UserEntity userentity = credentials.getUser();
			this.modelservice.delete(id, userentity);
			System.out.println("OKK");
			return "redirect:/default";
	 }
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(Model model) throws IOException {
		 ObjectMapper mapper = new ObjectMapper();
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
			UserEntity userentity = credentials.getUser();
    	String name = userentity.getFirst_name();
    	System.out.println(name.substring(name.length() - 1));
    	List<LessonManager> m = new ArrayList<>();
    	Json_reader interests = json("/json/user_model.json",true);
    	model.addAttribute("gender",name.substring(name.length() - 1));
		model.addAttribute("all_interests",interests.getInterests());
    	model.addAttribute("user",userentity);
    	model.addAttribute("credentials",credentials);
    	for(LessonEntity l : userentity.getFollowing_lessons()) {
    		String n = String.valueOf(l.getId()) + String.valueOf(userentity.getId());
    		LessonManager manager = MainController.LESSONS.get(n);
    		m.add(manager);	
   	}
   
    	model.addAttribute("manager",m);
    	model.addAttribute("teacher",true);
			return "admin/profilo";
	}
	
	
	public static Json_reader json(String input,Boolean help) {
		Json_reader interests=new Json_reader();
		System.out.println("username: ");
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<Json_reader> typeReference = new TypeReference<Json_reader>(){};
		InputStream inputStream = TypeReference.class.getResourceAsStream(input);
		System.out.println("username2: " + inputStream);
		
		try {
			 interests = mapper.readValue(inputStream,typeReference);
			 
			
		} catch (IOException e){
			System.out.println("Unable to save users: " + e.getMessage());
		}
		return interests;
		
	}
	
	@GetMapping(value = "/lezione/{id}")
	public String det_ordine(@PathVariable("id")Long id , Model model) throws JsonProcessingException {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity userentity = credentials.getUser();
		System.out.println("USER: " + userentity.getId());
    	model.addAttribute("user",userentity);
    	model.addAttribute("role",credentials.getRole());
		LessonEntity lezione = lessonservice.lezionePerId(id);
		model.addAttribute("files",lezione.getFiles());
		model.addAttribute("lezione",lezione);
		DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		String formattedDate = df.format(Calendar.getInstance().getTime());
		DateFormat df2 = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		System.out.println(formattedDate + "/" + (((Calendar.getInstance().get(Calendar.YEAR)+1))%100));
		model.addAttribute("anno",formattedDate + "/" + (((Calendar.getInstance().get(Calendar.YEAR)+1))%100));
		model.addAttribute("students",lezione.getFollowed_by());
		if(credentials.getRole().equals(STUDENT_ROLE)) { 
			String n = String.valueOf(id) + String.valueOf(userentity.getId());
			System.out.println("PROVA : " + MainController.LESSONS);
		model.addAttribute("messages",MainController.LESSONS.get(n).getStimuli(userentity.getId()));
		//send.notify(Starter.mapper.writeValueAsString(MainController.LESSONS.get(id).st), UserController.ONLINE.get(userentity.getId()));	
		}
	
		
		return "teachers/lezione";
	}
	
	@GetMapping(value = "/Argomento/{id}")
	public String det_Arg(@PathVariable("id")Long id , Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialservice.getCredentials(userDetails.getUsername());
		UserEntity userentity = credentials.getUser();
		DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		String formattedDate = df.format(Calendar.getInstance().getTime());
		DateFormat df2 = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		System.out.println(formattedDate + "/" + (((Calendar.getInstance().get(Calendar.YEAR)+1))%100));
		ModelEntity m = modelservice.getModel(id);
		model.addAttribute("anno",formattedDate + "/" + (((Calendar.getInstance().get(Calendar.YEAR)+1))%100));
		model.addAttribute("students",credentialservice.getTeacher("STUDENT"));
		model.addAttribute("arg",m);
		model.addAttribute("name", m.getName());
		model.addAttribute("goal",m.getRules());
		model.addAttribute("lesson",this.lessonservice.getlessonbymodel(this.modelservice.getModel(id)));
		model.addAttribute("user",userentity);
		
		
		return "teachers/Argomento";
	}
	
	@GetMapping(value = "/profile/{id}")
	public String det_profilo(@PathVariable(required = false) Long id, Model model) {
		UserEntity user = userservice.getUserId(id); 
		Credentials c = credentialservice.getCredentialsUser(id);
    	model.addAttribute("user",user);
    	model.addAttribute("teacher",false);
    	model.addAttribute("credentials",c);
		return "admin/profilo";
	}
	
	
}
