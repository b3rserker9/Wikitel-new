package it.cnr.istc.psts.wikitel.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import it.cnr.istc.psts.wikitel.db.UserEntity;
import lombok.Data;

@Entity
@Data
public class LessonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne
    private ModelEntity model;
    @ManyToOne
    @JsonBackReference
    private UserEntity teacher;
    @ManyToMany
    @JsonManagedReference
    private final Collection<UserEntity> followed_by = new ArrayList<>();
    @OneToMany
    private final Collection<RuleEntity> goals = new ArrayList<>();
    

    

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
