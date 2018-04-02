package com.example.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.model.AreaModel;
import com.example.model.ChangePasswordModel;
import com.example.model.ModelMap;
import com.example.model.NewUserModel;
import com.example.model.ResetPasswordModel;

/**
 * @author Subham Ashish (subham@sdrc.co.in)
 *
 */
public interface UserManagementService {

	String enableUserName(Integer userId);

	String disableUserName(Integer userId);

	boolean addNewUser(NewUserModel newUserModel);

	List<Role> displayAllRoles();

	 Map<Integer, List<ModelMap>> getRoleBasedFeaturePermission(Integer roleId);

	String deleteFeaturePermission(Integer roleFeaturePermissionId);

	String enableFeaturePermission(Integer roleFeaturePermissionId);

	Map<String, List<User>> getAllUser();

	Map<String,List<AreaModel>> getAllAreaList();

	List<User> displayAllUser();

	boolean addNewFeaturePermission(Integer roleId, Integer featurePermissionId);

	ResponseEntity<Boolean> resetPassword(ResetPasswordModel resetPasswordModel);

	ResponseEntity<String> changePassoword(ChangePasswordModel changePasswordModel);

	ResponseEntity<String> forgotPassword(String userName,String otp);

	ResponseEntity<String> sendOtp(String userName);

	ResponseEntity<String> validateOtp(String username, String otp);

	boolean disableRole(Integer roleId);

	boolean enableRole(Integer roleId);


}
