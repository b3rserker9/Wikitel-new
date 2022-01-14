package it.cnr.istc.psts.wikitel.Repository;

import org.springframework.data.repository.CrudRepository;

import it.cnr.istc.psts.wikitel.db.LessonEntity;
import it.cnr.istc.psts.wikitel.db.SuggestionEntity;

public interface SuggestionRepository extends  CrudRepository<SuggestionEntity, Long>{

}
