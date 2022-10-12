package it.cnr.istc.psts.wikitel.Mongodb;

import it.cnr.istc.psts.wikitel.db.RuleEntity;
import lombok.Data;

@Data
public class SuggestionMongo implements Comparable<SuggestionMongo>  {

	
	private String page;
	
	  private Double score;
	    private Double score2;
		@Override
		public int compareTo(SuggestionMongo o) {
			
			return this.getPage().compareTo(o.getPage());
		}
}
