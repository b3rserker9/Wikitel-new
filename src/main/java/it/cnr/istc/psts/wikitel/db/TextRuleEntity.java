package it.cnr.istc.psts.wikitel.db;

import javax.persistence.Entity;

@Entity
public class TextRuleEntity extends RuleEntity {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }
}
