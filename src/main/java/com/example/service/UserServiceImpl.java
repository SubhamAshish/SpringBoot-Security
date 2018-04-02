package com.example.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public User findByUsername(String userName) {
		
		return userRepository.findByUserName(userName);
		
	}

	@Transactional
	public void updateLoggedOutStatus(long userLoginMetaId, Timestamp loggedOutDateTime) {

		// while server start up
	}

}
