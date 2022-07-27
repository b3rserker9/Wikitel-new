package it.cnr.istc.psts.wikitel.Mongodb;


import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import it.cnr.istc.psts.wikitel.db.RuleEntity;
import lombok.Data;

@Document("Rule")
public class RuleMongo {
	
	
	@Id
	private String id;
	
	private String title;
	
	private Long length;
	
	private  Set<SuggestionMongo> suggestions = new HashSet<>();
	 
	private Set<String> topics = new HashSet<>();
	
	
	
	private boolean top_down = true;
	 
	  
	public boolean isTopDown() {
	        return top_down;
	    }
	  
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}



	public Set<SuggestionMongo> getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(Set<SuggestionMongo> suggestions) {
		this.suggestions = suggestions;
	}

	public Set<String> getTopics() {
		return topics;
	}

	public void setTopics(Set<String> topics) {
		this.topics = topics;
	}



	public boolean isTop_down() {
		return top_down;
	}

	public void setTop_down(boolean top_down) {
		this.top_down = top_down;
	}

	

}
