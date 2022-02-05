package it.cnr.psts.wikitel.API;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class LessonModel {

    private final long id;
    private final String name;
    private final Map<Long, Rule> rules;

    @JsonCreator
    public LessonModel(@JsonProperty("id") final long id, @JsonProperty("name") final String name,
            @JsonProperty("rules") final Map<Long, Rule> rules) {
        this.id = id;
        this.name = name;
        this.rules = rules;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<Long, Rule> getRules() {
        if (rules == null)
            return null;
        return Collections.unmodifiableMap(rules);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({ @Type(value = Rule.TextRule.class, name = "text"), @Type(value = Rule.WebRule.class, name = "web"),
            @Type(value = Rule.WikiRule.class, name = "wiki") })
    public static abstract class Rule {

        private final long id;
        private final String name;
        private final Set<String> topics;
        private final Long length;
        private final boolean top_down;
        private final Set<Long> preconditions;
        private final List<Suggestion> suggestions;

        @JsonCreator
        public Rule(@JsonProperty("id") final long id, @JsonProperty("name") final String name,
                @JsonProperty("topics") final Set<String> topics, @JsonProperty("length") final Long length,
                @JsonProperty("top_down") final boolean top_down,
                @JsonProperty("preconditions") final Set<Long> preconditions,
                @JsonProperty("suggestions") final List<Suggestion> suggestions) {
            this.id = id;
            this.name = name;
            this.topics = topics;
            this.length = length;
            this.top_down = top_down;
            this.preconditions = preconditions;
            this.suggestions = suggestions;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Set<String> getTopics() {
            if (topics == null)
                return null;
            return Collections.unmodifiableSet(topics);
        }

        public Long getLength() {
            return length;
        }

        public boolean isTopDown() {
            return top_down;
        }

        public Set<Long> getPreconditions() {
            if (preconditions == null)
                return null;
            return Collections.unmodifiableSet(preconditions);
        }

        public List<Suggestion> getSuggestions() {
            if (suggestions == null)
                return null;
            return Collections.unmodifiableList(suggestions);
        }

        public static class TextRule extends Rule {

            private final String text;

            @JsonCreator
            public TextRule(@JsonProperty("id") final long id, @JsonProperty("name") final String name,
                    @JsonProperty("topics") final Set<String> topics, @JsonProperty("length") final Long length,
                    @JsonProperty("top_down") final boolean top_down,
                    @JsonProperty("preconditions") final Set<Long> preconditions,
                    @JsonProperty("suggestions") final List<Suggestion> suggestions,
                    @JsonProperty("text") final String text) {
                super(id, name, topics, length, top_down, preconditions, suggestions);
                this.text = text;
            }

            public String getText() {
                return text;
            }
        }

        public static class WebRule extends Rule {

            private final String url;

            @JsonCreator
            public WebRule(@JsonProperty("id") final long id, @JsonProperty("name") final String name,
                    @JsonProperty("topics") final Set<String> topics, @JsonProperty("length") final Long length,
                    @JsonProperty("top_down") final boolean top_down,
                    @JsonProperty("preconditions") final Set<Long> preconditions,
                    @JsonProperty("suggestions") final List<Suggestion> suggestions,
                    @JsonProperty("url") final String url) {
                super(id, name, topics, length, top_down, preconditions, suggestions);
                this.url = url;
            }

            public String getUrl() {
                return url;
            }
        }

        public static class WikiRule extends Rule {

            private final String url;

            @JsonCreator
            public WikiRule(@JsonProperty("id") final long id, @JsonProperty("name") final String name,
                    @JsonProperty("topics") final Set<String> topics, @JsonProperty("length") final Long length,
                    @JsonProperty("top_down") final boolean top_down,
                    @JsonProperty("preconditions") final Set<Long> preconditions,
                    @JsonProperty("suggestions") final List<Suggestion> suggestions,
                    @JsonProperty("url") final String url) {
                super(id, name, topics, length, top_down, preconditions, suggestions);
                this.url = url;
            }

            public String getUrl() {
                return url;
            }
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({ @Type(value = Suggestion.WikiSuggestion.class, name = "wiki") })
    public static abstract class Suggestion {

        private final long id;
        private final double score;

        @JsonCreator
        public Suggestion(@JsonProperty("id") final long id, @JsonProperty("score") final double score) {
            this.id = id;
            this.score = score;
        }

        public long getId() {
            return id;
        }

        public double getScore() {
            return score;
        }

        public static class WikiSuggestion extends Suggestion {

            private final String page;

            @JsonCreator
            public WikiSuggestion(@JsonProperty("id") final long id, @JsonProperty("score") final double score,
                    @JsonProperty("page") final String page) {
                super(id, score);
                this.page = page;
            }

            public String getPage() {
                return page;
            }
        }
    }
}
