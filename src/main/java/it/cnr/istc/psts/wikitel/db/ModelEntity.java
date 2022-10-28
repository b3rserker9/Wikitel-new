package it.cnr.istc.psts.wikitel.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import it.cnr.istc.psts.wikitel.Mongodb.RuleMongo;
import lombok.Data;

@Entity


public class ModelEntity {

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<UserEntity> getTeachers() {
		return teachers;
	}

	public Collection<RuleEntity> getRules() {
		return rules;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    
    @ManyToMany
    @JsonBackReference
    private final Collection<UserEntity> teachers = new ArrayList<>();
    
    
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
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