package it.cnr.istc.psts.wikitel.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("prova32")
public class Provamongo {

	  @Id
      private String id;
	  
	  private String name;

	public Provamongo(String name) {
		super();
		this.name = name;
	}
	  @Override
	  public String toString() {
	    return String.format(
	        "Customer[id=%s, firstName='%s']",
	        id, name);
	  }
}
