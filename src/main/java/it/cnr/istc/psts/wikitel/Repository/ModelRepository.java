package it.cnr.istc.psts.wikitel.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.cnr.istc.psts.wikitel.db.LessonEntity;
import it.cnr.istc.psts.wikitel.db.ModelEntity;
import it.cnr.istc.psts.wikitel.db.UserEntity;

public interface ModelRepository extends CrudRepository<ModelEntity, Long> {

	public List<ModelEntity> findByTeachers(UserEntity teacher);
	
	public Optional<ModelEntity> findById(Long id);
	
	public Optional<ModelEntity>  findByName(String name);
	
}
