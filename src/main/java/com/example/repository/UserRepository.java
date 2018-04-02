package com.example.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Cacheable("users")
	User findByUserName(String username);

	User findByUserId(Integer id);

	@Query("select user from User user WHERE user.userName=:userName OR user.email=:userName")
	User getUserByUserNameOrEmail(@Param("userName") String userName);

	

}
