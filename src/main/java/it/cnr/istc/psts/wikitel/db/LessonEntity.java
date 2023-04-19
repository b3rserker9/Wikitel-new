package it.cnr.istc.psts.wikitel.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.web.bind.annotation.MatrixVariable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import it.cnr.istc.psts.wikitel.Mongodb.RuleMongo;
import it.cnr.istc.psts.wikitel.db.UserEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LessonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    private Boolean Async;
    @ManyToOne
    private ModelEntity model;
    @ManyToOne
    @JsonBackReference
    private UserEntity teacher;
    @ManyToMany
    @JsonManagedReference
    private final Collection<UserEntity> followed_by = new ArrayList<>();
    @ManyToMany
    private final Collection<RuleEntity> goals = new ArrayList<>();
    
    @Transient
    private Collection<RuleMongo> rule;
    
    
    @ManyToMany
    private List<Files> files = new ArrayList<>();

    public void addStudent(final UserEntity student) {
        followed_by.add(student);
    }

    public void removeStudent(final UserEntity student) {
        followed_by.remove(student);
    }

    
   
    public void addGoal(final RuleEntity goal) {
        goals.add(goal);
    }

    public void removeGoal(final RuleEntity goal) {
        goals.remove(goal);
    }
}
