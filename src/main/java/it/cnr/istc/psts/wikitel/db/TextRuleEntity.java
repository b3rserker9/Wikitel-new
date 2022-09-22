package it.cnr.istc.psts.wikitel.db;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TextRuleEntity extends RuleEntity {

	@Column(columnDefinition = "TEXT")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }
}
