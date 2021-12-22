package it.cnr.istc.psts.wikitel.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.cnr.istc.psts.wikitel.Repository.UserRepository;
import it.cnr.istc.psts.wikitel.db.UserEntity;

@Service
public class UserService {

	 @Autowired
	    protected PasswordEncoder passwordEncoder;
	
	 @Autowired
	    protected UserRepository userRepository;
	
	@Transactional
    public UserEntity saveUser(UserEntity utente) {
        return this.userRepository.save(utente);
    }
	
	@Transactional
	public UserEntity getUser(String email) {
		Optional<UserEntity> result = this.userRepository.findByEmail(email); 
		return result.orElse(null);
	}
	
	@Transactional
	public UserEntity getUserId(Long id) {
		Optional<UserEntity> result = this.userRepository.findById(id); 
		return result.orElse(null);
	}
	
	@Transactional
	public List<UserEntity> getTeacher(String role) {
		List<UserEntity> result = this.userRepository.findByRole(role); 
		return result;
	}
	
}
