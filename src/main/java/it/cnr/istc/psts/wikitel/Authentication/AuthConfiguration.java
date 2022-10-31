package it.cnr.istc.psts.wikitel.Authentication;

import static it.cnr.istc.psts.wikitel.db.UserEntity.STUDENT_ROLE;
import static it.cnr.istc.psts.wikitel.db.UserEntity.TEACHER_ROLE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.socket.WebSocketSession;


import it.cnr.istc.psts.WikitelNewApplication;
import it.cnr.istc.psts.wikitel.Service.UserService;
import it.cnr.istc.psts.wikitel.controller.MainController;
import it.cnr.istc.psts.wikitel.controller.UserController;
import it.cnr.istc.psts.wikitel.controller.pageController;

/**
 * The AuthConfiguration is a Spring Security Configuration.
 * It extends WebSecurityConfigurerAdapter, meaning that it provides the settings for Web security.
 */

@Configuration
@EnableWebSecurity
public class AuthConfiguration extends WebSecurityConfigurerAdapter { 

	/**
     * The datasource is automatically injected into the AuthConfiguration (using its getters and setters)
     * and it is used to access the DB to get the Credentials to perform authentiation and authorization
     */
	
    @Autowired
    DataSource datasource;
    @Autowired
    private SimpMessagingTemplate webSocket;
   
    
    
    private final MessageSendingOperations<String> messageSendingOperations = null;
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
 
  
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
        http
        		// authorization paragraph: qui definiamo chi può accedere a cosa
                .authorizeRequests()
                // chiunque (autenticato o no) può accedere alle pagine index, login, register, ai css e alle immagini
                .antMatchers(HttpMethod.GET, "/", "/index", "/css/**","/js/**", "/images/**","/json","/lessons","/deletemodel","/verify").permitAll()
                // chiunque (autenticato o no) può mandare richieste POST al punto di accesso per login e register 
                .antMatchers(HttpMethod.POST,  "/register", "/getEmail").permitAll()
                // solo gli utenti autenticati con ruolo ADMIN possono accedere a risorse con path /admin/**
                .antMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(STUDENT_ROLE)
                .antMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(STUDENT_ROLE)
                
                .antMatchers(HttpMethod.GET, "/teachers/**","/NewLesson").hasAnyAuthority(TEACHER_ROLE)
                .antMatchers(HttpMethod.POST, "/teachers/**","/NewLesson","/provamessaggio").hasAnyAuthority(TEACHER_ROLE)
                // tutti gli utenti autenticati possono accere alle pagine rimanenti 
                .anyRequest().authenticated().and().formLogin()
                .loginPage("/index").usernameParameter("email").failureUrl("/index?error=true").
                defaultSuccessUrl("/default")
                .successHandler(new AuthenticationSuccessHandler() {
					
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						System.out.println("prova253");
					response.sendRedirect("/default");
						
					}
				})

                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
					
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
							throws IOException, ServletException {
						UserController.ONLINE.remove(authentication.getName());
						response.sendRedirect("/");
						
					}
				})
                .invalidateHttpSession(true)
                .clearAuthentication(true).permitAll().and().csrf().disable();

                    
               
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.jdbcAuthentication()
        .dataSource(this.datasource)
        .authoritiesByUsernameQuery("SELECT email, role FROM credentials WHERE email=?")
        .usersByUsernameQuery("SELECT email, password, 1 as enabled FROM credentials WHERE email=?");
}

    /**
     * This method defines a "passwordEncoder" Bean.
     * The passwordEncoder Bean is used to encrypt and decrpyt the Credentials passwords.
     */
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}