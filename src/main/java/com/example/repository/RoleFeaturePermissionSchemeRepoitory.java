package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domain.Role;
import com.example.domain.RoleFeaturePermissionScheme;

public interface RoleFeaturePermissionSchemeRepoitory extends
	JpaRepository<RoleFeaturePermissionScheme, Integer> {

	List<RoleFeaturePermissionScheme> findByRole(Role role);

	RoleFeaturePermissionScheme findByRoleFeaturePermissionSchemeId(Integer roleFeaturePermissionId);

	//List<RoleFeaturePermissionScheme> findByRoleFeaturePermissionSchemeIdOrderByAsc();
	
	

}
