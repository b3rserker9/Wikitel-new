package it.cnr.istc.psts.wikitel.Mongodb;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import it.cnr.istc.psts.wikitel.db.RuleEntity;
import lombok.Data;

@Entity
@Data
public class RuleString extends RuleEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	  private Long peopleId;

	  
	  private String string;
	  
	  private String name;
	  
	  private String url;


	public RuleString(String string) {
		super();
		this.string = string;
	}


	public RuleString() {
		super();
	}
	  
	  
}
