package it.cnr.istc.psts.wikitel.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.cnr.istc.psts.wikitel.db.ModelEntity;
import it.cnr.istc.psts.wikitel.db.RuleEntity;

public interface RuleRepository extends  CrudRepository<RuleEntity, Long> {
	
	public Optional<RuleEntity> findById(Long id);

}
