package it.cnr.istc.psts.wikitel.Repository;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;

import it.cnr.istc.psts.wikitel.db.User;
import it.cnr.istc.psts.wikitel.db.UserEntity;
import it.cnr.istc.psts.wikitel.db.LessonEntity;
import it.cnr.istc.psts.wikitel.db.ModelEntity;
import it.cnr.istc.psts.wikitel.db.RuleEntity;
import lombok.Data;

@Data
public class Response {
	private String rule_name;
	private String status;
	private Long rule;
	private Long model;
	private User data;
	private UserEntity data2;
	private LessonEntity data4;
	private List<LessonEntity> data5;
	private Long data6;
	private ModelEntity data7;
	private List<ModelEntity> data8;
	private Boolean exists; String suggest; List<String> maybe ; Set<String> topics;   
	private Exception e;
	
	public Response(String status, Exception e) {
		this.status=status;
		this.e=e;
	}
	
	public Response(String status, User user) {
		this.status=status;
		this.data=user;
	}
	
	public Response(String status, UserEntity user) {
		this.status=status;
		this.data2=user;
	}	
	
	public Response(Long rule, Long model ) {
		this.rule = rule;
		this.model = model;
	}
	
	public Response(String status, LessonEntity lesson ) {
		this.status=status;
		this.data4=lesson;
	}
	
	public Response(String status, ModelEntity model ) {
		this.status=status;
		this.data7=model;
	}
	public  Response(String status, List<LessonEntity> lessons) {
		this.status=status;
		this.data5=lessons;
	}
	public Response(Boolean status2, Long id, Long rule_id, String rule_name) {
		this.exists=status2;
		this.data6=rule_id;
		this.model= id;
		this.rule_name=rule_name;
	}    
	public Response(String status2, Long id) {
		this.status=status2;
		this.data6=id;
	}   
	public Response(Boolean status2, Long id) {
		this.exists=status2;
		this.data6=id;
	}
	
	public Response(String status2) {
		this.status=status2;
	}
	public Response(String status2,String rule_name) {
		this.status=status2;
		this.rule_name =rule_name;
	}
	
	public Response(String id ,Set<String> maybe) {
		this.status=id;
		this.topics=maybe;
	} 
	
	public Response(Boolean exits, String suggest, List<String> maybe,Long model) {
		this.exists=exits;
		this.suggest=suggest;
		this.maybe = maybe;
		this.model = model;
	}  
	
	
}
