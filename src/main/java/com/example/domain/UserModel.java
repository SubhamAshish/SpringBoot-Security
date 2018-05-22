package com.example.domain;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;



public class UserModel extends User {

	private static final long serialVersionUID = 3070385867750194519L;

	// add all extra required details for your application
	private Integer userId;

	/*private List<String> roles;*/
	
	Collection<UserAreaMapping> areaMappings;
	
	private LoginUserDetails userLoginMetaId;
	
	// Using private constructor to force initialization of extra parameters
	private UserModel(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	public UserModel(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Integer userId,Collection<UserAreaMapping> areaMappings) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.areaMappings = areaMappings;
		this.userId = userId;
	}


	public Collection<UserAreaMapping> getAreaMappings() {
		return areaMappings;
	}

	public void setAreaMappings(Collection<UserAreaMapping> areaMappings) {
		this.areaMappings = areaMappings;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public LoginUserDetails getUserLoginMetaId() {
		return userLoginMetaId;
	}

	public void setUserLoginMetaId(LoginUserDetails userLoginMetaId) {
		this.userLoginMetaId = userLoginMetaId;
	}

	/*public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}*/

	
	
}
