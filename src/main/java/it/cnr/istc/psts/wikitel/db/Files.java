package it.cnr.istc.psts.wikitel.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Files {
	
    public Files(String name) {
		super();
		this.name = name; 
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	public Files(String name, String src) {
		super();
		this.name = name;
		this.src = src;
	}

	public Files() {
		super();
	}

	private String name;
	
	private String src;

}
