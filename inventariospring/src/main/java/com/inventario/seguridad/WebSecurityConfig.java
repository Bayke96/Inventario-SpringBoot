package com.inventario.seguridad;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;


@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
 
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select acc_usuario, acc_contraseña, true from Cuentas where acc_usuario = ?")
				.authoritiesByUsernameQuery("SELECT acc_usuario, 'ROLE_USER' FROM Cuentas WHERE acc_usuario = ?")
				.passwordEncoder(new BCryptPasswordEncoder(12));
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

	    http.antMatcher("/**")
	    	.authorizeRequests()
	        .antMatchers("/", "/login**", "/css/**", "/images/**", "/registrarse", "/scripts/**",
	        		"/inventario/usuario/**", "/error-ingreso")
	         .permitAll()
	         .anyRequest()
	         .authenticated()
	         .and()
	         .formLogin().loginPage("/login").
	         defaultSuccessUrl("/productos/1").failureUrl("/error-ingreso").
	         usernameParameter("usuario").passwordParameter("contraseña")
	         .and().logout().logoutUrl("/salir");
	        
	    http.headers().xssProtection();
	        
	}
	 
}