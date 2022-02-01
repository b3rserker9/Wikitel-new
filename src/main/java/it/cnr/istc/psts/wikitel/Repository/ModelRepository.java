package it.cnr.istc.psts.wikitel.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.cnr.istc.psts.wikitel.db.LessonEntity;
import it.cnr.istc.psts.wikitel.db.Model;
import it.cnr.istc.psts.wikitel.db.ModelEntity;
import it.cnr.istc.psts.wikitel.db.UserEntity;

public interface ModelRepository extends CrudRepository<ModelEntity, Long> {

	public List<ModelEntity> findByTeachers(UserEntity teacher);
	
	public Optional<ModelEntity> findById(Long id);
	
	public Optional<ModelEntity>  findByName(String name);
	
	
	@Query(value ="SELECT m.id,m.name FROM model_entity m JOIN model_entity_teachers t ON (m.id = t.models_id) WHERE t.teachers_id = ?", nativeQuery = true)
	public List<Model> findByTeachersonly(Long teachers);
	
}
