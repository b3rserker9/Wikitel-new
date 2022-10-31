package it.cnr.istc.psts.wikitel.db;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(indexes = { @Index(name = "email_index", columnList = "email", unique = true) })
public class Credentials {
	
	public static final String TEACHER_ROLE = "TEACHER";
	public static final String STUDENT_ROLE = "STUDENT";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
    private String email;
	
	   @Column(nullable = false)
	    private String password;
	   
	   @Column(nullable = false)
		private String role;
	   
	   @OneToOne(cascade = CascadeType.ALL)
		private UserEntity user;
	   

	    @Column(name = "verification_code", length = 64)
	    private String verificationCode;
	     
	    private boolean enabled;
}
