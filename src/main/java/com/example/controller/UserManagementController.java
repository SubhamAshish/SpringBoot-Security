package com.example.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.model.AreaModel;
import com.example.model.ChangePasswordModel;
import com.example.model.ModelMap;
import com.example.model.NewUserModel;
import com.example.model.ResetPasswordModel;
import com.example.service.UserManagementService;
import com.google.gson.Gson;


/**
 * @author Subham Ashish (subham@sdrc.co.in)
 *
 */
@Controller
public class UserManagementController {
	
	@Autowired
	private UserManagementService userManagementService;
	
	@RequestMapping(value="/addUser",method=RequestMethod.GET)
	public String adduser(){
		
		return "adduser";
		
	}
	
	@RequestMapping(value="/welcomePage")
	public String welcome(){
		return "welcome";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
	
	@RequestMapping(value="/manageRoles",method=RequestMethod.GET)
	public String rolePage(){
		return "rolePage";
	}
	
	
	//after login
	@PreAuthorize("hasAuthority('Administrator:Setting Feature,Edit')")
	@RequestMapping(value="/adminPage")
	public String adminpage(){
		return "adminpage";
	}
	
	@PreAuthorize("hasAuthority('Editor:Setting Feature,Edit')")
	@RequestMapping(value="/otheruserpage")
	public String otheruserpage(){
		return "otheruserpage";
	}
	//end
	
	
	//=====================================
	/*
	 * API Begins(for user management)
	 */
	
	@RequestMapping(value="/allRole")
	@ResponseBody
	public List<Role> getAllRoles(){
		
		return userManagementService.displayAllRoles();
		
	}
	
	
	//role wise user display
	@RequestMapping(value="/allUser",method=RequestMethod.GET)
	@ResponseBody
	Map<String, List<User>> displayAllUser(){
		
			return userManagementService.getAllUser();
			
	}
	
	
	//List of all users
	@RequestMapping(value="/getAllUser")
	@ResponseBody
	public List<User> getAllUser(){
		
		return userManagementService.displayAllUser();
		
	}
	
	/**
	 * it unables user when user is disabled by admin
	 * @param username
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/enableUser",method=RequestMethod.POST)
	@ResponseBody
	public String enableUser(@RequestParam("userId") Integer userId){
		
		String result =  userManagementService.enableUserName(userId);
		
		Gson g = new Gson();
		
		return g.toJson(result);
		
	}
	
	/**
	 * it disable users whenever admin wants to disable
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/disableUser",method=RequestMethod.POST)
	@ResponseBody
	public String disableUser(@RequestParam("userId") Integer userId){
		
		
		String result =  userManagementService.disableUserName(userId);
		
		Gson g = new Gson();
		
		return g.toJson(result);
		
	}
	
	/**
	 * it
	 * @param userName
	 * @param password
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="/addUser",method=RequestMethod.POST)
	@ResponseBody
	public boolean addUser(@RequestBody NewUserModel newUserModel){
		
		return userManagementService.addNewUser(newUserModel);
	}
	

	//role-having what feature and permission.
	@RequestMapping(value="/rolefeature",method=RequestMethod.GET)
	@ResponseBody
	 Map<Integer, List<ModelMap>> roleFeaturePermission(@RequestParam("roleId") Integer roleId){
		
		
		return userManagementService.getRoleBasedFeaturePermission(roleId);
	}
	
	
	//disable roles
	@RequestMapping(value="/disableRole",method=RequestMethod.POST)
	@ResponseBody
	public boolean disableRole(@RequestParam("roleId") Integer roleId){
		
		return userManagementService.disableRole(roleId);
	}
	
	//
	@RequestMapping(value="/enableRole",method=RequestMethod.POST)
	@ResponseBody
	public boolean enableRole(@RequestParam("roleId") Integer roleId){
		
		return userManagementService.enableRole(roleId);
	}
	
	
	//RoleFeaturePermissionId to disable permission for a particular feature
	@RequestMapping(value="/disableFeaturePermission",method=RequestMethod.POST)
	@ResponseBody
	public String disableFeaturePermission(@RequestParam("roleFeaturePermissionId") Integer roleFeaturePermissionId){
		
		String result =  userManagementService.deleteFeaturePermission(roleFeaturePermissionId);
	
		Gson g = new Gson();
		
		return g.toJson(result);
	}
	
	
	//RoleFeaturePermissionId to enable permission for a particular feature
	@RequestMapping(value="/enableFeaturePermission",method=RequestMethod.POST)
	@ResponseBody
	public String enableFeaturePermission(@RequestParam("roleFeaturePermissionId") Integer roleFeaturePermissionId){
			
		String result =  userManagementService.enableFeaturePermission(roleFeaturePermissionId);
		
		Gson g = new Gson();
	
		return g.toJson(result);
			
	}

	
	/** 
	 * @return arealevel name as a key!
	 */
	@ResponseBody
	@RequestMapping(value="/getAllArea")
	Map<String,List<AreaModel>> displayArea(){
		
		return userManagementService.getAllAreaList();
		
	}
	
	//add new feature permission to given role
	@ResponseBody
	@RequestMapping(value="/addNewFeaturePermission",method=RequestMethod.POST)
	boolean addNewFeaturePermission(@RequestParam("roleId") Integer roleId,@RequestParam("featurePermissionId") Integer featurePermissionId){
		
		return userManagementService.addNewFeaturePermission(roleId,featurePermissionId);
	}
	
	
	
	//reset password
	/**
	 * @param resetPasswordModel
	 * @return
	 */
	@RequestMapping(value="/resetPassword")
	@ResponseBody
	public ResponseEntity<Boolean> resetPassword(@RequestBody ResetPasswordModel resetPasswordModel){
		
		
		return  userManagementService.resetPassword(resetPasswordModel);

		
	}
	
	/**
	 * @param changePasswordModel
	 * @return
	 */
	@RequestMapping(value="/changePassword",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> changePassword(@RequestBody ChangePasswordModel changePasswordModel){
		
		return userManagementService.changePassoword(changePasswordModel);
	}
	
	
	//forgot password
	@RequestMapping(value="/forgotPassword",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String>  forgotPassword(@RequestParam("userName") String userName,@RequestParam(value="otp") String otp)	{
	
		ResponseEntity<String> result = userManagementService.forgotPassword(userName,otp);
		
		return result;
	}
	
	//otp generation
	@RequestMapping(value="/sendOtp")
	@ResponseBody
	public ResponseEntity<String> sendOtp(@RequestParam("userName") String userName){
		
		return userManagementService.sendOtp(userName);
		
	}

	
	//validating otp here
	@RequestMapping(value = "/validateOtp")
	@ResponseBody
	public ResponseEntity<String> validateOtp(@RequestParam("userName") String userName, @RequestParam("otp") String otp) {
		
		
	 return userManagementService.validateOtp(userName, otp);
		
		
	}

	
}
