package it.cnr.istc.psts.wikitel.db;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Document("prova23")
public class modelmongo {
	
	

	    @Id
	    private String id;
	    private String name;
	    
	    @ManyToMany
	    @JsonBackReference
	    private final Collection<UserEntity> teachers = new ArrayList<>();
	    
	    
	    @OneToMany(cascade = CascadeType.REMOVE)
	    @JsonManagedReference
	    private final Collection<RuleEntity> rules = new ArrayList<>();

	 

	    public void addTeacher(final UserEntity teacher) {
	        teachers.add(teacher);
	    }

	    public void removeTeacher(final UserEntity teacher) {
	        teachers.remove(teacher);
	    }


	    public void addRule(final RuleEntity rule) {
	        rules.add(rule);
	    }

	    public void removeRule(final RuleEntity rule) {
	        rules.remove(rule);
	    }
	
}
