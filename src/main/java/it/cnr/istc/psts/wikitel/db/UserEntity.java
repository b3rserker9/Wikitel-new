package it.cnr.istc.psts.wikitel.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import it.cnr.istc.psts.wikitel.controller.Interests;
import it.cnr.istc.psts.wikitel.controller.Json_reader;
import lombok.Data;
@Data
@Entity
@Table(indexes = { @Index(name = "email_index", columnList = "email", unique = true) })
public class UserEntity {
	
	public static final String TEACHER_ROLE = "TEACHER";
	public static final String STUDENT_ROLE = "STUDENT";
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String first_name;
    private String last_name;
    @Column(columnDefinition="text")
    private String profile;
    @Column(columnDefinition="text")
    private String src;
    
    @Column(columnDefinition="text")
    private String Questionario;
    
    @OneToMany
    private final Collection<FileEntity> file = new ArrayList<>();
    
    @Column(nullable = false)
	private String role;
    @OneToMany
    private final Collection<RuleEntity> learnt_topics = new ArrayList<>();
    @JsonManagedReference
    @ManyToMany(mappedBy = "teachers")
    
    private final Collection<ModelEntity> models = new ArrayList<>();
    @ManyToMany(mappedBy = "students")
    
    private final Collection<UserEntity> teachers = new ArrayList<>();
    @ManyToMany
    private final Collection<UserEntity> students = new ArrayList<>();
    @ManyToMany(mappedBy = "followed_by") 
    @JsonBackReference
    private final List<LessonEntity> following_lessons = new ArrayList<>();
    @OneToMany(mappedBy = "teacher", orphanRemoval = true)
    @JsonManagedReference
    private final List<LessonEntity> teaching_lessons = new ArrayList<>();

    
}
