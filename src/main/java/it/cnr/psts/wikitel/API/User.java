package it.cnr.psts.wikitel.API;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;



public class User {

    private final long id;
    private final String email;
    private final String first_name;
    private final String last_name;
    private final String profile;
    private final Map<Long, User> teachers;
    private final Map<Long, User> students;
    private final Map<Long, Lesson> following_lessons;
    private final Map<Long, Lesson> teaching_lessons;
    private final Map<Long, LessonModel> models;
    private final boolean online;

    @JsonCreator
    public User(@JsonProperty("id") final long id, @JsonProperty("email") final String email,
            @JsonProperty("firstName") final String first_name, @JsonProperty("lastName") final String last_name,
            @JsonProperty("profile") final String profile, @JsonProperty("teachers") final Map<Long, User> teachers,
            @JsonProperty("students") final Map<Long, User> students,
            @JsonProperty("followingLessons") final Map<Long, Lesson> following_lessons,
            @JsonProperty("teachingLessons") final Map<Long, Lesson> teaching_lessons,
            @JsonProperty("models") final Map<Long, LessonModel> models, @JsonProperty("online") final boolean online) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile = profile;
        this.teachers = teachers;
        this.students = students;
        this.following_lessons = following_lessons;
        this.teaching_lessons = teaching_lessons;
        this.models = models;
        this.online = online;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the first_name
     */
    public String getFirstName() {
        return first_name;
    }

    /**
     * @return the last_name
     */
    public String getLastName() {
        return last_name;
    }

    /**
     * @return the profile
     */
    public String getProfile() {
        return profile;
    }

    public Map<Long, User> getTeachers() {
        if (teachers == null)
            return null;
        return Collections.unmodifiableMap(teachers);
    }

    public Map<Long, User> getStudents() {
        if (students == null)
            return null;
        return Collections.unmodifiableMap(students);
    }

    /**
     * @return the following
     */
    public Map<Long, Lesson> getFollowingLessons() {
        if (following_lessons == null)
            return null;
        return Collections.unmodifiableMap(following_lessons);
    }

    /**
     * @return the teaching
     */
    public Map<Long, Lesson> getTeachingLessons() {
        if (teaching_lessons == null)
            return null;
        return Collections.unmodifiableMap(teaching_lessons);
    }

    public Map<Long, LessonModel> getModels() {
        if (models == null)
            return null;
        return Collections.unmodifiableMap(models);
    }

    /**
     * @return the online
     */
    public boolean isOnline() {
        return online;
    }
}
