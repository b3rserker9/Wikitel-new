package it.cnr.istc.psts.wikitel.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class ModelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToMany
    private final Collection<UserEntity> teachers = new ArrayList<>();
    @OneToMany(orphanRemoval = true)
    private final Collection<RuleEntity> rules = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Collection<UserEntity> getTeachers() {
        return Collections.unmodifiableCollection(teachers);
    }

    public void addTeacher(final UserEntity teacher) {
        teachers.add(teacher);
    }

    public void removeTeacher(final UserEntity teacher) {
        teachers.remove(teacher);
    }

    public Collection<RuleEntity> getRules() {
        return Collections.unmodifiableCollection(rules);
    }

    public void addRule(final RuleEntity rule) {
        rules.add(rule);
    }

    public void removeRule(final RuleEntity rule) {
        rules.remove(rule);
    }
}