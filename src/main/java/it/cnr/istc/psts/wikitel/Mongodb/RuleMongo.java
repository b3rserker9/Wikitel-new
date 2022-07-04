package it.cnr.istc.psts.wikitel.Mongodb;

import java.beans.Transient;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import it.cnr.istc.psts.wikitel.db.RuleEntity;
import it.cnr.istc.psts.wikitel.db.RuleSuggestionRelationEntity;
import lombok.Data;

@Document("Rule")
@Data
public class RuleMongo {
	
	@Id
	private String id;
	
	private String name;
	
	private  Set<RuleMongo> precondition = new HashSet<>();
	
	 private  Set<SuggestionMongo> suggestions = new HashSet<>();
	 
	

}
