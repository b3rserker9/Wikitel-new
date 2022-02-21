package it.cnr.istc.psts.wikitel.db;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FileRuleEntity extends RuleEntity {
	
	private String src;

}
