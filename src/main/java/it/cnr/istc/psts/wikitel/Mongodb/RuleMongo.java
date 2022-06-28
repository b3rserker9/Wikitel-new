package it.cnr.istc.psts.wikitel.Mongodb;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import it.cnr.istc.psts.wikitel.db.RuleSuggestionRelationEntity;
import lombok.Data;

@Document("Rule")
@Data
public class RuleMongo {
	
	@Id
	private String id;
	
	private String name;
	
	private final Set<RuleMongo> precondition = new HashSet<>();
	
	 private final Set<SuggestionMongo> suggestions = new HashSet<>();

}
