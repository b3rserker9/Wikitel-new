package it.cnr.istc.psts.wikitel.db;

import java.util.List;
import java.util.Set;

import lombok.Data;
@Data
public class Prova {

	private Set<String> categories;
	private long length;
	private List<String> preconditions;
	private List<Float> rank1;
	private List<Float> rank2;
	private String url;
	private Boolean exists;
	private String suggest;
	private List<String> maybe;
}
