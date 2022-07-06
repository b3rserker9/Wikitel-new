package it.cnr.istc.psts.wikitel.Mongodb;


import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("Rule")
@Data
public class RuleMongo {
	
	@Id
	private String id;
	
	private String title;
	
	private Long length;
	
	private  Set<RuleMongo> precondition = new HashSet<>();
	
	private  Set<SuggestionMongo> suggestions = new HashSet<>();
	 
	private Set<String> topics = new HashSet<>();
	 
	

}
