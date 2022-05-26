package it.cnr.istc.psts.wikitel.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.cnr.istc.psts.wikitel.db.LessonEntity;
import it.cnr.istc.psts.wikitel.db.Model;
import it.cnr.istc.psts.wikitel.db.User;
import it.cnr.istc.psts.wikitel.db.UserEntity;


public interface UserRepository extends CrudRepository<UserEntity, Long>{

	@Query(value ="SELECT u.id, u.first_name, u.last_name, u.questionario ,u.profile, u.src FROM user_entity u JOIN credentials c ON (u.id = c.user_id ) WHERE c.role = ?", nativeQuery = true)
	public List<UserEntity> findByRole(String role);
	
}
