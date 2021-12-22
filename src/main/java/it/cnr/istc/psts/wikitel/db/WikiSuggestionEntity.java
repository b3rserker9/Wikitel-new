package it.cnr.istc.psts.wikitel.db;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(columnList = "page", unique = true))
public class WikiSuggestionEntity extends SuggestionEntity {

    private String page;

    public String getPage() {
        return page;
    }

    public void setPage(final String page) {
        this.page = page;
    }
}
