package it.cnr.istc.psts.wikitel.Service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.cnr.istc.psts.wikitel.Repository.CredentialsRepository;
import it.cnr.istc.psts.wikitel.db.Credentials;
import it.cnr.istc.psts.wikitel.db.UserEntity;

@Service
public class CredentialService {
	
    @Autowired
    protected PasswordEncoder passwordEncoder;

	@Autowired
	protected CredentialsRepository credentialsRepository;
	
	@Transactional
	public Credentials getCredentials(Long id) {
		Optional<Credentials> result = this.credentialsRepository.findById(id);
		return result.orElse(null);
	}

	@Transactional
	public Credentials getCredentials(String username) {
		Optional<Credentials> result = this.credentialsRepository.findByEmail(username);
		return result.orElse(null);
	}
	
	@Transactional
	public Credentials getUser(String email) {
		Optional<Credentials> result = this.credentialsRepository.findByEmail(email); 
		return result.orElse(null);
	}
	
	@Transactional
	public List<Credentials> getTeacher(String role) {
		List<Credentials> result = this.credentialsRepository.findByRole(role); 
		return result;
	}
	
	
		
    @Transactional
    public Credentials save(Credentials credentials) {
        return this.credentialsRepository.save(credentials);
    }
    
}
