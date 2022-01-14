package it.cnr.istc.psts.wikitel.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Data;

@Entity
@Data
public class FileEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false, unique = true)
	private String name;
	
	private String type;
	
	private Long size;
	
	private Date dateUpload;
	
	private byte[] content;

	public FileEntity(String name, String type, byte[] content) {
		super();
		this.name = name;
		this.type = type;
		this.content = content;
	}

	public FileEntity() {
		super();
	}

	
	
	

}
