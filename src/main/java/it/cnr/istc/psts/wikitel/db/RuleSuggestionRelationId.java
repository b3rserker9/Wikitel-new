package it.cnr.istc.psts.wikitel.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class RuleSuggestionRelationId  implements Serializable{

    @Column(name = "RULE_ID")
    private Long rule_id;
    @Column(name = "SUGGESTION_ID")
    private Long suggestion_id;
}
