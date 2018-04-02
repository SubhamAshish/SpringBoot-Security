package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domain.LoginUserDetails;

public interface LoginUserDetailsRepository extends JpaRepository<LoginUserDetails, Integer> {

	LoginUserDetails findByLoginMetaId(Integer loginMetaId);

}
