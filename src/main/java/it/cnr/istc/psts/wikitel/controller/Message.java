package it.cnr.istc.psts.wikitel.controller;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @Type(value = Message.Stimulus.TextStimulus.class, name = "text-stimulus"),
        @Type(value = Message.Stimulus.QuestionStimulus.class, name = "question-stimulus"),
        @Type(value = Message.Stimulus.URLStimulus.class, name = "url-stimulus") })
public abstract class Message {
	
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

}
