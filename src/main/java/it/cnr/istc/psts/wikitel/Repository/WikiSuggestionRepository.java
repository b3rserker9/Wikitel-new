package it.cnr.istc.psts.wikitel.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.cnr.istc.psts.wikitel.db.UserEntity;
import it.cnr.istc.psts.wikitel.db.WikiSuggestionEntity;

public interface WikiSuggestionRepository extends CrudRepository<WikiSuggestionEntity, Long> {
	
	public Optional<WikiSuggestionEntity> findByPage(String page);
}
