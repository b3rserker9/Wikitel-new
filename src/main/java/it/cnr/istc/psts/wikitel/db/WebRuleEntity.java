package it.cnr.istc.psts.wikitel.db;

import javax.persistence.Entity;

@Entity
public class WebRuleEntity extends RuleEntity {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
