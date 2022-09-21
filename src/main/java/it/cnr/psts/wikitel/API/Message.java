package it.cnr.psts.wikitel.API;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import it.cnr.psts.wikitel.API.Lesson.LessonState;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = Message.Online.class, name = "online"),
        @Type(value = Message.Follower.class, name = "follower"),
        @Type(value = Message.Subscribe.class, name = "Subscribe") ,
        @Type(value = Message.ProfileUpdate.class, name = "profile-update"),
        @Type(value = Message.FollowLesson.class, name = "follow-lesson"),
        @Type(value = Message.UnfollowLesson.class, name = "unfollow-lesson"),
        @Type(value = Message.RemoveLesson.class, name = "remove-lesson"),
        @Type(value = Message.LessonStateUpdate.class, name = "lesson-state-update"),
        @Type(value = Message.Stimulus.TextStimulus.class, name = "text-stimulus"),
        @Type(value = Message.Stimulus.QuestionStimulus.class, name = "question-stimulus"),
        @Type(value = Message.Stimulus.URLStimulus.class, name = "url-stimulus"),
        @Type(value = Message.Searching.class, name = "searching"),
        @Type(value = Message.User.class, name = "user"),
        @Type(value = Message.Lesson.class, name = "lesson")})
public abstract class Message {

    /**
     * This message is used for communicating that a user is now online/offline.
     */
    public static class Online extends Message {

        private final long user;
        private final boolean online;

        @JsonCreator
        public Online(@JsonProperty("user") final long user, @JsonProperty("online") final boolean online) {
            this.user = user;
            this.online = online;
        }

        /**
         * @return the user.
         */
        public long getUser() {
            return user;
        }

        /**
         * @return the connected state.
         */
        public boolean isOnline() {
            return online;
        }
    }
    
    /**
     * This message is used for communicating that a user is now online/offline.
     */
    public static class Subscribe extends Message {

        private final long lesson_id;
        private final String lesson;

        @JsonCreator
        public Subscribe( @JsonProperty("lesson_id") final long lesson_id,  @JsonProperty("lesson") final String lesson) {
            this.lesson_id = lesson_id;
            this.lesson = lesson;
        }

   

        /**
         * @return the connected state.
         */
        public long getLessonId() {
            return lesson_id;
        }
        
        /**
         * @return the connected state.
         */
        public String getLesson() {
            return lesson;
        }
    }

    /**
     * This message is used for communicating to a teacher that a student has been
     * added/removed.
     */
    public static class Follower extends Message {

        private final long student;
        private final boolean added;

        @JsonCreator
        public Follower(@JsonProperty("student") final long student, @JsonProperty("added") final boolean added) {
            this.student = student;
            this.added = added;
        }

        /**
         * @return the student.
         */
        public long getStudent() {
            return student;
        }

        /**
         * @return the added state.
         */
        public boolean isAdded() {
            return added;
        }
    }

    /**
     * This message is used for communicating that a user profile has been updated.
     */
    public static class ProfileUpdate extends Message {

        private final long user;
        private final String profile;

        @JsonCreator
        public ProfileUpdate(@JsonProperty("user") long user, @JsonProperty("profile") String profile) {
            this.user = user;
            this.profile = profile;
        }

        public long getUser() {
            return user;
        }

        public String getProfile() {
            return profile;
        }
    }

    /**
     * This message is used for communicating that a student is following a lesson.
     */
    public static class FollowLesson extends Message {

        private final User student;
        private final long lesson;
        private final Set<String> interests; // interests are here to allow their definition within the lesson model..

        @JsonCreator
        public FollowLesson(@JsonProperty("student") final User student, @JsonProperty("lesson") final long lesson,
                @JsonProperty("interests") final Set<String> interests) {
            this.student = student;
            this.lesson = lesson;
            this.interests = interests;
        }

        /**
         * @return the following student.
         */
        public User getStudent() {
            return student;
        }

        /**
         * @return the followed lesson.
         */
        public long getLesson() {
            return lesson;
        }

        /**
         * @return the user's interests.
         */
        public Set<String> getInterests() {
            return Collections.unmodifiableSet(interests);
        }
    }

    /**
     * This message is used for communicating that a student is not following a
     * lesson anymore.
     */
    public static class UnfollowLesson extends Message {

        private final long student;
        private final long lesson;

        @JsonCreator
        public UnfollowLesson(@JsonProperty("student") final long student, @JsonProperty("lesson") final long lesson) {
            this.student = student;
            this.lesson = lesson;
        }

        /**
         * @return the unfollowing student.
         */
        public long getStudent() {
            return student;
        }

        /**
         * @return the unfollowed lesson.
         */
        public long getLesson() {
            return lesson;
        }
    }

    /**
     * This message is used for communicating that a lesson has been removed.
     */
    public static class RemoveLesson extends Message {

        private final long lesson;

        @JsonCreator
        public RemoveLesson(@JsonProperty("lesson") final long lesson) {
            this.lesson = lesson;
        }

        /**
         * @return the removed lesson.
         */
        public long getLesson() {
            return lesson;
        }
    }

    /**
     * This message is used for communicating that a lesson has changed its state.
     */
    public static class LessonStateUpdate extends Message {

        private final long lesson;
        private final LessonState state;

        @JsonCreator
        public LessonStateUpdate(@JsonProperty("lesson") final long lesson,
                @JsonProperty("state") final LessonState state) {
            this.lesson = lesson;
            this.state = state;
        }

        /**
         * @return the removed lesson.
         */
        public long getLesson() {
            return lesson;
        }

        /**
         * @return the new lesson's state.
         */
        public LessonState getState() {
            return state;
        }
    }

    /**
     * This message is used for communicating the creation of a new stimulus. This
     * is the base class for representing stimuli.
     */
    public abstract static class Stimulus extends Message {

        private final long lesson_id;
        private final long id;
        private final long time;
        private final boolean read;
        private final String name;

        @JsonCreator
        public Stimulus(@JsonProperty("lessonId") final long lesson_id, @JsonProperty("id") final long id,
                @JsonProperty("time") final long time, @JsonProperty("read") final boolean read,
                @JsonProperty("name") final String name) {
            this.lesson_id = lesson_id;
            this.id = id;
            this.time = time;
            this.read = read;
            this.name = name;
        }

        /**
         * @return the lesson id
         */
        public long getLesson_id() {
            return lesson_id;
        }

        /**
         * @return the id
         */
        public long getId() {
            return id;
        }

        /**
         * @return the time
         */
        public long getTime() {
            return time;
        }

        /**
         * @return the read state
         */
        public boolean isRead() {
            return read;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * This message is used for communicating the creation of a new text stimulus.
         */
        public static class TextStimulus extends Stimulus {

            @JsonCreator
            public TextStimulus(@JsonProperty("lessonId") final long lesson_id, @JsonProperty("id") final long id,
                    @JsonProperty("time") final long time, @JsonProperty("read") final boolean read,
                    @JsonProperty("name") final String name) {
                super(lesson_id, id, time, read, name);
            }
        }

        /**
         * This message is used for communicating the creation of a new question
         * stimulus.
         */
        public static class QuestionStimulus extends Stimulus {

            private final List<String> answers;
            private final Integer answer;

            @JsonCreator
            public QuestionStimulus(@JsonProperty("lessonId") final long lesson_id, @JsonProperty("id") final long id,
                    @JsonProperty("time") final long time, @JsonProperty("read") final boolean read,
                    @JsonProperty("name") final String name, @JsonProperty("answers") final List<String> answers,
                    @JsonProperty("answer") final Integer answer) {
                super(lesson_id, id, time, read, name);
                this.answers = answers;
                this.answer = answer;
            }

            /**
             * @return the answers
             */
            public List<String> getAnswers() {
                return Collections.unmodifiableList(answers);
            }

            /**
             * @return the answer
             */
            public Integer getAnswer() {
                return answer;
            }

            public static class Answer extends Message {

                private final long lesson_id;
                private final long question_id;
                private final long answer;

                @JsonCreator
                public Answer(@JsonProperty("lessonId") final long lesson_id,
                        @JsonProperty("question_id") final long question_id,
                        @JsonProperty("answer") final long answer) {
                    this.lesson_id = lesson_id;
                    this.question_id = question_id;
                    this.answer = answer;
                }

                /**
                 * @return the lesson id
                 */
                public long getLessonId() {
                    return lesson_id;
                }

                /**
                 * @return the question id
                 */
                public long getQuestionId() {
                    return question_id;
                }

                /**
                 * @return the answer
                 */
                public long getAnswer() {
                    return answer;
                }
            }
        }

        /**
         * This message is used for communicating the creation of a new url stimulus.
         */
        public static class URLStimulus extends Stimulus {

            private final String url;

            @JsonCreator
            public URLStimulus(@JsonProperty("lessonId") final long lesson_id, @JsonProperty("id") final long id,
                    @JsonProperty("time") final long time, @JsonProperty("read") final boolean read,
                    @JsonProperty("name") final String name, @JsonProperty("url") final String url) {
                super(lesson_id, id, time, read, name);
                this.url = url;
            }

            /**
             * @return the url
             */
            public String getUrl() {
                return url;
            }
        }
    }

    /**
     * This message is used for communicating the removal of an existing stimulus
     * from a lesson.
     */
    public static class RemoveStimulus extends Message {

        private final long lesson_id;
        private final long id;

        @JsonCreator
        public RemoveStimulus(@JsonProperty("lessonId") final long lesson_id, @JsonProperty("id") final long id) {
            this.lesson_id = lesson_id;
            this.id = id;
        }

        /**
         * @return the lesson_id
         */
        public long getLessonId() {
            return lesson_id;
        }

        /**
         * @return the id
         */
        public long getId() {
            return id;
        }
    }
    
    public static class Searching extends Message {

        private final String name;
        private final long status;

        @JsonCreator
        public Searching(@JsonProperty("Name") final String name, @JsonProperty("Status") final long status) {
            this.name = name;
            this.status = status;
        }

        /**
         * @return the lesson_id
         */
        public String getName() {
            return name;
        }

        /**
         * @return the id
         */
        public long getStatus() {
            return status;
        }
    }
    
    public static class User extends Message {

        private final Long id_user;

        @JsonCreator
        public User(@JsonProperty("id_user") final Long id_user) {
            this.id_user = id_user;
        }

        /**
         * @return the lesson_id
         */
        public Long getId() {
            return id_user;
        }

    }
    
    public static class Lesson extends Message {

        private final Long id_Lesson;
        private final String name_lesson;
        private final String teacher_lesson; 
        private final Long teacher_id;
        @JsonCreator
        public Lesson(@JsonProperty("id_Lesson") final Long id_Lesson, @JsonProperty("name_lesson") final String name_lesson ,@JsonProperty("teacher_lesson") final String teacher_lesson, @JsonProperty("teacher_lesson") final Long teacher_id) {
            this.id_Lesson = id_Lesson;
            this.name_lesson = name_lesson;
            this.teacher_lesson= teacher_lesson;
            this.teacher_id = teacher_id;
        }

        /**
         * @return the lesson_id
         */
        public Long getid_Lesson() {
            return id_Lesson;
        }
        public Long getteacher_id() {
            return teacher_id;
        }
        
        public String getteacher_lesson() {
            return teacher_lesson;
        }
        
        public String getname_lesson() {
            return name_lesson;
        }

    }
    
}