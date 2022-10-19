package it.cnr.istc.psts.wikitel.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.cnr.istc.psts.wikitel.db.ModelEntity;
import it.cnr.istc.psts.wikitel.db.RuleEntity;

public interface RuleRepository extends  CrudRepository<RuleEntity, Long> {
	
	public Optional<RuleEntity> findById(Long id);
	
	@Query(value = "Select name from rule_entity where id= ?", nativeQuery = true)
	public String findNameById(Long id);

	@Query(value = "select text from rule_entity where id=?" , nativeQuery = true)
	public String findNameBytext(Long id);
	
	@Query(value = "select src from rule_entity where id=?" , nativeQuery = true)
	public String findNameByfile(Long id);
	
	public List<RuleEntity> findByName(String name);
}
