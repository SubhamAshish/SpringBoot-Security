package com.example.service;

import com.example.domain.User;

public interface UserService {

	User findByUsername(String userName);
	
}
