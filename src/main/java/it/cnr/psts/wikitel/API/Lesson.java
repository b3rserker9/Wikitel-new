package it.cnr.psts.wikitel.API;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.cnr.psts.wikitel.API.Message.Stimulus;


public class Lesson {

    private final long id;
    private final String name;
    private final long model_id;
    private final Set<String> topics;
    private final User teacher;
    private final Map<Long, User> students;
    private final Collection<Stimulus> stimuli;
    private LessonState state;
    private long time;

    @JsonCreator
    public Lesson(@JsonProperty("id") final long id, @JsonProperty("name") final String name,
            @JsonProperty("modelId") final long model_id, @JsonProperty("topics") final Set<String> topics,
            @JsonProperty("teacher") final User teacher, @JsonProperty("students") final Map<Long, User> students,
            @JsonProperty("stimuli") final Collection<Message.Stimulus> stimuli,
            @JsonProperty("state") final LessonState state, @JsonProperty("time") final long time) {
        this.id = id;
        this.name = name;
        this.model_id = model_id;
        this.topics = topics;
        this.teacher = teacher;
        this.students = students;
        this.stimuli = stimuli;
        this.state = state;
        this.time = time;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the model id
     */
    public long getModelId() {
        return model_id;
    }

    /**
     * @return the topics
     */
    public Set<String> getTopics() {
        if (topics == null)
            return null;
        return Collections.unmodifiableSet(topics);
    }

    /**
     * @return the teacher
     */
    public User getTeacher() {
        return teacher;
    }

    /**
     * @return the students
     */
    public Map<Long, User> getStudents() {
        if (students == null)
            return null;
        return Collections.unmodifiableMap(students);
    }

    /**
     * @return the stimuli
     */
    public Collection<Message.Stimulus> getStimuli() {
        if (stimuli == null)
            return null;
        return Collections.unmodifiableCollection(stimuli);
    }

    /**
     * @return the state
     */
    public LessonState getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(final LessonState state) {
        this.state = state;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(final long time) {
        this.time = time;
    }

    public enum LessonState {
        Running, Paused, Stopped
    }
}
