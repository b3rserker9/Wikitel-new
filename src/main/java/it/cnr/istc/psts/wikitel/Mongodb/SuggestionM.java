package it.cnr.istc.psts.wikitel.Mongodb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("Wikitel Suggestion")
public class SuggestionM {
	
	@Id
	private String id;

	private List<SuggestionMongo> suggestion = new ArrayList();
	
	
}
