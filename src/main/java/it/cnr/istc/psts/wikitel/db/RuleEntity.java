package it.cnr.istc.psts.wikitel.db;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class RuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private boolean top_down = true;
    @ElementCollection
    private final Set<String> topics = new HashSet<>();
    private Long length;
    @ManyToMany(mappedBy = "effects")
    private final Set<RuleEntity> preconditions = new HashSet<>();
    @ManyToMany
    private final Set<RuleEntity> effects = new HashSet<>();
    @OneToMany(orphanRemoval = true)
    private final Set<RuleSuggestionRelationEntity> suggestions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTopDown() {
        return top_down;
    }

    public void setTopDown(boolean top_down) {
        this.top_down = top_down;
    }

    public Set<String> getTopics() {
        return Collections.unmodifiableSet(topics);
    }

    public void addTopic(final String topic) {
        topics.add(topic);
    }

    public void removeTopic(final String topic) {
        topics.remove(topic);
    }

    public Long getLength() {
        return length;
    }

    public void setLength(final Long length) {
        this.length = length;
    }

    public Set<RuleEntity> getPreconditions() {
        return Collections.unmodifiableSet(preconditions);
    }

    public void addPrecondition(final RuleEntity precondition) {
        preconditions.add(precondition);
    }

    public void removePrecondition(final RuleEntity precondition) {
        preconditions.remove(precondition);
    }

    public Set<RuleEntity> getEffects() {
        return Collections.unmodifiableSet(effects);
    }

    public void addEffect(final RuleEntity effect) {
        effects.add(effect);
    }

    public void removeEffect(final RuleEntity effect) {
        effects.remove(effect);
    }

    public Set<RuleSuggestionRelationEntity> getSuggestions() {
        return Collections.unmodifiableSet(suggestions);
    }

    public void addSuggestion(final RuleSuggestionRelationEntity suggestion) {
        suggestions.add(suggestion);
    }

    public void removeSuggestion(final RuleSuggestionRelationEntity suggestion) {
        suggestions.remove(suggestion);
    }
}
