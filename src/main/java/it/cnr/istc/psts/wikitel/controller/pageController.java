package it.cnr.istc.psts.wikitel.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.cnr.istc.pst.oratio.Item.ArithItem;
import it.cnr.istc.pst.oratio.Solver;
import it.cnr.istc.pst.oratio.SolverException;
import it.cnr.istc.psts.wikitel.Service.LessonService;
import it.cnr.istc.psts.wikitel.Service.UserService;
import it.cnr.istc.psts.wikitel.db.LessonEntity;
import it.cnr.istc.psts.wikitel.db.User;
import it.cnr.istc.psts.wikitel.db.UserEntity;
import it.cnr.istc.psts.wikitell.LessonManager;






@Controller
public class pageController {
	
	@Autowired
	private UserService userservice;
	
	@Autowired
	private LessonService lessonservice;
	
	Solver solver = new Solver();

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) throws NoSuchFieldException, SolverException {
			
		Json_reader interests = json("/json/user_model.json",true);
		model.addAttribute("interests", interests.getInterests());
			return "index";
	}
	
	@RequestMapping(value = "/failure", method = RequestMethod.GET)
	public String failure(Model model) {
		Json_reader interests = json("/json/user_model.json",true);
		model.addAttribute("interests", interests.getInterests());
		model.addAttribute("loginError", true);
			return "index";
	}
	
	@RequestMapping(value = "/default", method = RequestMethod.GET)
    public String defaultAfterLogin(Model model) {
		Json_reader interests = json("/json/user_model.json",true);
		System.out.println("username1:PIPPO");
    	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	System.out.println("username2:PIPPO");
    	UserEntity userentity =  userservice.getUser(userDetails.getUsername());
    	model.addAttribute("interests", interests.getInterests());
    	model.addAttribute("user",userentity);
    	model.addAttribute("lessons",lessonservice.getlesson(userentity));
    	model.addAttribute("Teachers",userservice.getTeacher(userentity.TEACHER_ROLE));
    	//se e' admin
    	if (userentity.getRole().equals(UserEntity.STUDENT_ROLE)) {
    		System.out.println("username:PIPPO");
            return "admin/hello";
        }
    	//se non lo e'
        return "teachers/index";
    }
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEntity userentity =  userservice.getUser(userDetails.getUsername());
    	Json_reader interests = json("/json/user_"+ userentity.getId() +".json",false);
    	model.addAttribute("interests", interests.getInterests());
    	model.addAttribute("user",userentity);
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
	public String det_ordine(@PathVariable("id")Long id , Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	UserEntity userentity =  userservice.getUser(userDetails.getUsername());
		LessonEntity lezione = lessonservice.lezionePerId(id);
		model.addAttribute("lezione",lezione);
		DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		String formattedDate = df.format(Calendar.getInstance().getTime());
		DateFormat df2 = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		System.out.println(formattedDate + "/" + (((Calendar.getInstance().get(Calendar.YEAR)+1))%100));
		model.addAttribute("anno",formattedDate + "/" + (((Calendar.getInstance().get(Calendar.YEAR)+1))%100));
		model.addAttribute("students",lezione.getFollowed_by());
		
		return "teachers/lezione";
	}
	
	@GetMapping(value = "/profile/{id}")
	public String det_profilo(@PathVariable(required = false) Long id, Model model) {
		UserEntity user = userservice.getUserId(id);
		Json_reader interests = json("/json/user_"+ user.getId() +".json",false);
    	model.addAttribute("interests", interests.getInterests());
    	model.addAttribute("user",user);
    	model.addAttribute("teacher",false);
		return "admin/profilo";
	}
	
	
}
