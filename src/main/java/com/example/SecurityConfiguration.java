package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.example.security.UserAuthenticationProvider;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private  UserAuthenticationProvider userAuthenticationProvider;
	

	@Autowired
	private void configureGlobal(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(userAuthenticationProvider);
	}

	 

}
