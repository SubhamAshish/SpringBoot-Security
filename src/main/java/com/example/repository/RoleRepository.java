package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

	Role findByRoleId(int roleId);


	List<Role> findByOrderByRoleIdAsc();

	

}
