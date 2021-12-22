package it.cnr.istc.psts.wikitel.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.cnr.istc.psts.wikitel.db.LessonEntity;
import it.cnr.istc.psts.wikitel.db.UserEntity;


public interface LessonsRepository extends CrudRepository<LessonEntity, Long>{
	
	public List<LessonEntity> findByTeacher(UserEntity teacher);
	
	public Optional<LessonEntity> findById(Long id);
	
	public Optional<LessonEntity>  findByName(String name);

}
