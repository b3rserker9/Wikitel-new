package it.cnr.istc.psts.wikitel.Repository;


	import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.cnr.istc.psts.wikitel.db.Credentials;
import it.cnr.istc.psts.wikitel.db.UserEntity;


	public interface CredentialsRepository extends CrudRepository<Credentials, Long> {
		
		public Optional<Credentials> findByEmail(String username);
		
		
		
		public List<Credentials> findByRole(String role);
		
		@Query(value ="SELECT u.id,u.first_name, u. last_name FROM credentials c JOIN user_entity u ON (c.user = u.id) WHERE c.role = ?", nativeQuery = true)
		public List<UserEntity> findByRoleSpec(String role);
		
		@Query(value ="SELECT * FROM credentials c WHERE c.user_id = ?", nativeQuery = true)
		public Optional<Credentials> findByuser(Long id);

	}
	
