package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domain.UserRoleFeaturePermissionMapping;

public interface UserRoleFeaturePermissionMappingRepository extends JpaRepository<UserRoleFeaturePermissionMapping, Integer>{

	
//	Iterable<UserRoleFeaturePermissionMappingRepository> save(Iterable<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappingList);

	

}
