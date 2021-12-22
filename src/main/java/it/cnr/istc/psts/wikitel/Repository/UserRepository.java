package it.cnr.istc.psts.wikitel.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.cnr.istc.psts.wikitel.db.UserEntity;


public interface UserRepository extends CrudRepository<UserEntity, Long>{

	public Optional<UserEntity> findByEmail(String email);
	
	public List<UserEntity> findByRole(String role);
	
}
