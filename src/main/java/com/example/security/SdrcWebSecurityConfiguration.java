package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.example.controller.CustomLogoutSuccessHandler;
import com.example.controller.LoginSuccessHandler;


@Configuration
@Order(2)
public class SdrcWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private  UserAuthenticationProvider userAuthenticationProvider;

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	
	@Autowired
	private CustomLogoutSuccessHandler customLogoutSuccessHandler;
	
	@Autowired
	private void configureGlobal(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(userAuthenticationProvider);
	}
	
	
	 @Override
	 public void configure(HttpSecurity http) throws Exception {
	
			http.authorizeRequests()
			.antMatchers("/resources/**", "/signup", "/about",
					"/login","/allUser","/addUser","/allRole","/enableUser",
					"/disableUser","/rolefeature","/manageRoles","/disableRole",
					"/enableFeaturePermission","/disableFeaturePermission","/api/**","/getAllArea",
					"/addNewFeaturePermission","/changePassword","/forgotPassword","/enableRole","/sendOtp","/validateOtp","/forgotPassword")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
			.and().formLogin().loginPage("/login").successHandler(loginSuccessHandler).and().logout().logoutSuccessHandler(customLogoutSuccessHandler);
			
			http.csrf().disable();
			
	 }
	 
	 
	 
}
