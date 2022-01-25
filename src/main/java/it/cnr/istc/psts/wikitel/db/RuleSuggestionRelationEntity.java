package it.cnr.istc.psts.wikitel.db;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
public class RuleSuggestionRelationEntity {

    @EmbeddedId
    private RuleSuggestionRelationId id;
    @ManyToOne
    @MapsId("rule_id")
    @JoinColumn(name = "RULE_ID")
    @JsonBackReference
    private RuleEntity rule;
    @ManyToOne
    @MapsId("suggestion_id")
    @JoinColumn(name = "SUGGESTION_ID")
    private SuggestionEntity suggestion;
    private Double score;
    private Double score2;

    public RuleSuggestionRelationId getId() {
        return id;
    }

    public void setId(final RuleSuggestionRelationId id) {
        this.id = id;
    }

    public RuleEntity getRule() {
        return rule;
    }

    public void setRule(RuleEntity rule) {
        this.rule = rule;
    }

    public SuggestionEntity getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(SuggestionEntity suggestion) {
        this.suggestion = suggestion;
    }

    public Double getScore() {
        return score;
    }
    public Double getScore2() {
        return score2;
    }

    public void setScore(final Double score) {
        this.score = score;
    }
    public void setScore2(final Double score) {
        this.score2 = score;
    }
}
