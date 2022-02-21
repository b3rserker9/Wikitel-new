package it.cnr.istc.psts.wikitel.controller;

import lombok.Data;

@Data
public class Session {
	
	private String session;
	private Long user_id;
	private Long lesson_id;

}
