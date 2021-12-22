package it.cnr.istc.psts.wikitel.Repository;

import java.util.List;

import org.springframework.http.HttpStatus;

import it.cnr.istc.psts.wikitel.db.User;
import it.cnr.istc.psts.wikitel.db.UserEntity;
import it.cnr.istc.psts.wikitel.db.Email;
import it.cnr.istc.psts.wikitel.db.LessonEntity;
import lombok.Data;

@Data
public class Response {

	private String status;
	private User data;
	private UserEntity data2;
	private Email data3;
	private LessonEntity data4;
	private List<LessonEntity> data5;
	private Long data6;
	
	public Response(String status, User user) {
		this.status=status;
		this.data=user;
	}
	public Response(String status, UserEntity user) {
		this.status=status;
		this.data2=user;
	}
	
	public Response(String status, Email email ) {
		this.status=status;
		this.data3=email;
	}
	
	public Response(String status, LessonEntity lesson ) {
		this.status=status;
		this.data4=lesson;
	}
	public Response(String status, List<LessonEntity> lessons) {
		this.status=status;
		this.data5=lessons;
	}
	public Response(String status2, Long id) {
		this.status=status2;
		this.data6=id;
	}
	public Response(String status2) {
		this.status=status2;
	}

}
