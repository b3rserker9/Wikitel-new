package it.cnr.istc.psts.wikitel.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Questionario {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
	 
	 @Column
	 	private int  activeOrreflexive;
	 	
	 @Column
	 	private int praticalOrintuitive;
	 	
	 @Column
	 	private int visualOrverbal;
	 	
	 @Column
	 	private int sequentialOrglobal;

}
