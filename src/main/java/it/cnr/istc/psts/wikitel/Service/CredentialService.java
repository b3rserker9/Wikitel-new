package it.cnr.istc.psts.wikitel.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Transactional
	public Credentials getCredentials(Long id) {
		Optional<Credentials> result = this.credentialsRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Credentials getCredentialsUser(Long id) {
		Optional<Credentials> result = this.credentialsRepository.findByuser(id);
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
	public boolean verify(String verificationCode) {
	    Credentials c = this.credentialsRepository.findByVerificationCode(verificationCode);
	     
	    if (c == null || c.isEnabled()) {
	        return false;
	    } else {
	        c.setVerificationCode(null);
	        c.setEnabled(true);
	        this.save(c);
	         
	        return true;
	    }
	     
	}
	
	 public void sendVerificationEmail(Credentials user, String siteURL) 
		 throws MessagingException, UnsupportedEncodingException {
			    String toAddress = user.getEmail();
			    String fromAddress = "wikitelromatrecnr@gmail.com";
			    String senderName = "WikiTEL";
			    String subject = "Verifica email";
			    ;
			    //String url = "http://roma3ailab.it:7000/Confirmation/";
			    String content = "Ciao " + user.getUser().getLast_name() + " " + user.getUser().getFirst_name()+ ",<br>"
			            + "Ecco il link di Wikitel necessario per verificare l'account " +user.getEmail()+":<br>"
			            + "Hai registrato un account su WikiTEL, prima di poter utilizzare il tuo account devi verificare che questo sia il tuo indirizzo email cliccando qui: <br>"
			            + "<h3><a href=" + siteURL+ ">VERIFY</a></h3>"
			            + "Cordiali saluti, WikiTEL.<br>";
			            
			     
			    MimeMessage message = mailSender.createMimeMessage();
			    MimeMessageHelper helper = new MimeMessageHelper(message);
			     
			    helper.setFrom(fromAddress, senderName);
			    helper.setTo(toAddress);
			    helper.setSubject(subject);
			     
			    content = content.replace("[[name]]", user.getUser().getLast_name() + user.getUser().getFirst_name() );
			    
			     
			    helper.setText(content, true);
			     
			    mailSender.send(message);
	    }
	
		
    @Transactional
    public Credentials save(Credentials credentials) {
        return this.credentialsRepository.save(credentials);
    }
    
}
