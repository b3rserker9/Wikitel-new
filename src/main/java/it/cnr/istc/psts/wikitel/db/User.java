package it.cnr.istc.psts.wikitel.db;

import java.util.ArrayList;
import java.util.List;

import it.cnr.istc.psts.wikitel.controller.Interests;
import lombok.Data;

@Data
public class User  {

	private String email;
	private String password;
    private String first_name;
    private String last_name;
    private String profile;
	
}
