package com.example.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Area;
import com.example.domain.FeaturePermissionMapping;
import com.example.domain.Role;
import com.example.domain.RoleFeaturePermissionScheme;
import com.example.domain.User;
import com.example.domain.UserAreaMapping;
import com.example.domain.UserRoleFeaturePermissionMapping;
import com.example.model.AreaModel;
import com.example.model.ChangePasswordModel;
import com.example.model.Mail;
import com.example.model.ModelMap;
import com.example.model.NewUserModel;
import com.example.model.ResetPasswordModel;
import com.example.repository.AreaRepository;
import com.example.repository.RoleFeaturePermissionSchemeRepoitory;
import com.example.repository.RoleRepository;
import com.example.repository.UserAreaMappingRepository;
import com.example.repository.UserRepository;
import com.example.repository.UserRoleFeaturePermissionMappingRepository;
import com.google.gson.Gson;

/**
 * @author Subham Ashish (subham@sdrc.co.in)
 *
 */
@Service
public class UserManagementServiceImpl implements UserManagementService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleFeaturePermissionSchemeRepoitory roleFeaturePermissionSchemeRepoitory;

	@Autowired
	private UserRoleFeaturePermissionMappingRepository userRoleFeaturePermissionMappingRepository;
	
	@Autowired
	private UserAreaMappingRepository userAreaMappingRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private MessageDigestPasswordEncoder messageDigestPasswordEncoder;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ConfigurableEnvironment  configurableEnvironment;

	
	//making role name as a key
	@Override
	public Map<String, List<User>> getAllUser() {
		
		List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappingList = userRoleFeaturePermissionMappingRepository.findAll();
		
		//IGNORING such list whose role is ADMIN
		List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappingFilteredList = userRoleFeaturePermissionMappingList.stream().
				filter(roleName->!"ADMIN".equals(roleName.getUserAreaMapping().
						getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionScheme().getRole().getRoleName()))
				.collect(Collectors.toList());
		
		
		Map<String, List<User>> userMap = new LinkedHashMap<String, List<User>>();
		
		List<User> userList = new ArrayList<User>();
		
		for(UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping : userRoleFeaturePermissionMappingFilteredList){
			
			if(userMap.containsKey(userRoleFeaturePermissionMapping.getUserAreaMapping()
					.getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionScheme().getRole().getRoleName())){
				
				//userList.add(userRoleFeaturePermissionMapping.getUserAreaMapping().getUser());
				
				userMap.get(userRoleFeaturePermissionMapping.getUserAreaMapping().getUserRoleFeaturePermissionMappings().
						get(0).getRoleFeaturePermissionScheme().getRole().getRoleName()).
				add(userRoleFeaturePermissionMapping.getUserAreaMapping().getUser());
				
				
			}else{
				
				userList = new ArrayList<User>();
				
				userList.add(userRoleFeaturePermissionMapping.getUserAreaMapping().getUser());
				
				userMap.put(userRoleFeaturePermissionMapping.getUserAreaMapping()
					.getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionScheme().getRole().getRoleName(), userList);
				
			}
			
		}
		
		return userMap;
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public List<User> displayAllUser() {

		List<User> userList = userRepository.findAll();
		
		return userList;

	}
	
	@Override
	public String enableUserName(Integer userId) {

		User user = userRepository.findByUserId(userId);

		if(user.isEnabled()==true)
			return "already enabled";
		else{
			
			user.setEnabled(true);
			
		}

		User saveUser = userRepository.save(user);

		if (saveUser != null) {
			
			return "successfull";
			
		}else{
			throw new RuntimeException();
		}

	}

	@Override
	public String disableUserName(Integer userId) {

		User user = userRepository.findByUserId(userId);
		
		if(user.isEnabled() == false){
			return "already disable";
		}
		
		user.setEnabled(false);
		

		User saveUser = userRepository.save(user);

		if (saveUser != null) {
			return "successfull";
		}else
			throw new RuntimeException();

	}

	@Override
	@Transactional
	public boolean addNewUser(NewUserModel newUserModel) {

		Role role = new Role(newUserModel.getRoleId());

		User user = new User();

		user.setUserName(newUserModel.getUserName());
		user.setPassword(messageDigestPasswordEncoder.encodePassword(newUserModel.getUserName(), newUserModel.getPassword()));
		user.setCredentialexpired(false);
		user.setEnabled(true);
		user.setExpired(false);
		user.setLocked(false);

		User saveUser = userRepository.save(user);

		if (saveUser != null) {

			// set this user for which area
			// here area is harcoded
			Area area = new Area(newUserModel.getAreaId());
			
			Timestamp createdDate =  new Timestamp(new java.util.Date().getTime());
			
			UserAreaMapping UserAreaMapping = new UserAreaMapping();
			UserAreaMapping.setUser(saveUser);
			UserAreaMapping.setArea(area);
			UserAreaMapping.setCreatedDate(createdDate);
			UserAreaMapping saveUserAreaMapping = userAreaMappingRepository.save(UserAreaMapping);

			if (saveUserAreaMapping != null) {

				// find feature-permission for that role
				List<RoleFeaturePermissionScheme> roleFeaturePermissionScheme = roleFeaturePermissionSchemeRepoitory.findByRole(role);
				
				List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappingList = new ArrayList<UserRoleFeaturePermissionMapping>();
				if (!roleFeaturePermissionScheme.isEmpty()) {
					
				for(RoleFeaturePermissionScheme rfps : roleFeaturePermissionScheme){
					
					
					UserRoleFeaturePermissionMapping UserRoleFeaturePermissionMapping = new UserRoleFeaturePermissionMapping();

					UserRoleFeaturePermissionMapping.setRoleFeaturePermissionScheme(rfps);
					UserRoleFeaturePermissionMapping.setUserAreaMapping(saveUserAreaMapping);
					
					userRoleFeaturePermissionMappingList.add(UserRoleFeaturePermissionMapping);

				}
				
				List<UserRoleFeaturePermissionMapping> saveUserRoleFeaturePermissionMapping = userRoleFeaturePermissionMappingRepository.save(userRoleFeaturePermissionMappingList);
				
				if(!saveUserRoleFeaturePermissionMapping.isEmpty()){
					return true;
				}else
					throw new RuntimeException();
				
				}else
					throw new RuntimeException();
			}else
				throw new RuntimeException();

		}else
			throw new RuntimeException();

	}

	@Override
	public List<Role> displayAllRoles() {
		
		List<Role> roleList = roleRepository.findByOrderByRoleIdAsc();
		
		//IGNORING such list whose role is ADMIN
		
		List<Role> role = roleList.stream().filter(roleName->!"ADMIN".equals(roleName.getRoleName())).collect(Collectors.toList());
				
		return role;
		
	}

	
	/* 
	 * getting all feature permission to the selected role-(which is already mapped) making is_live true and all the unmapped 
	 * feature-permission setting is_live false so that we can add that feature permission to selected role
	 * 
	 * is_enable is for to know whether that role-feature-permission(for selected role) is enabled or disabled
	 * is_enable=true means enabled
	 */
	@Override
	@Transactional
	public Map<Integer, List<ModelMap>> getRoleBasedFeaturePermission(Integer roleId) {
		
		Role role = new Role(roleId);
		
		List<RoleFeaturePermissionScheme> selectedRoleFeaturePermissionSchemeList = roleFeaturePermissionSchemeRepoitory.findByRole(role);
		
		//all role-feature-permission
		List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemeList= roleFeaturePermissionSchemeRepoitory.findAll();
		
		
		//IGNORING such list whose role is ADMIN
		List<RoleFeaturePermissionScheme> roleFeaturePermissionList = roleFeaturePermissionSchemeList.stream().filter
						(featureList->!"ADMIN".equals(featureList.getRole().getRoleName())).collect(Collectors.toList());
		
		
		
		ModelMap modelMap = new ModelMap();
		
		List<ModelMap> modelList = new ArrayList<>();
		
		Map<Integer,List<ModelMap>> mapModel = new LinkedHashMap<>();
		
		
		List<Integer> featurePermissionCheckList = new ArrayList<>();
		
		
		
		for(RoleFeaturePermissionScheme rfp : selectedRoleFeaturePermissionSchemeList){
			
					if(mapModel.containsKey(rfp.getRoleFeaturePermissionSchemeId())){
				
						
								//true if this feature-permission is given to selected role if not than set false
								modelMap.setLive(true);
								modelMap.setEnable(rfp.getIsLive());
								modelMap.setFeaturePermission(rfp.getFeaturePermissionMapping());
								
								mapModel.get(rfp.getRoleFeaturePermissionSchemeId()).add(modelMap);
								
								featurePermissionCheckList.add(rfp.getFeaturePermissionMapping().getFeaturePermissionId());
								//modelList.add(modelMap);
					
					
				
					}else{
				
						modelList = new ArrayList<>();
						modelMap = new ModelMap();
						
					
						modelMap.setLive(true);
						modelMap.setEnable(rfp.getIsLive());
						modelMap.setFeaturePermission(rfp.getFeaturePermissionMapping());
						modelList.add(modelMap);
					
						mapModel.put(rfp.getRoleFeaturePermissionSchemeId(), modelList);
								
						featurePermissionCheckList.add(rfp.getFeaturePermissionMapping().getFeaturePermissionId());
					
							
					}
		
		}
		
		
		//looking for that feature permission which is not matched to selected role but available in database
		for(RoleFeaturePermissionScheme scheme  : roleFeaturePermissionList){
			
			if(featurePermissionCheckList.stream().allMatch(v->v.intValue()!=scheme.getFeaturePermissionMapping().getFeaturePermissionId())){
				
				modelList = new ArrayList<>();
				modelMap = new ModelMap();
				modelMap.setLive(false);
				modelMap.setEnable(scheme.getIsLive());
				modelMap.setFeaturePermission(scheme.getFeaturePermissionMapping());
				modelList.add(modelMap);
				
				mapModel.put(scheme.getRoleFeaturePermissionSchemeId(), modelList);
				
				featurePermissionCheckList.add(scheme.getFeaturePermissionMapping().getFeaturePermissionId());
				
			}
			
		}
		
		
		return mapModel;
		
	}

	
	@Override
	@Transactional
	public boolean disableRole(Integer roleId) {
		
		Role role = roleRepository.findByRoleId(roleId);
		
			role.setEnable(false);
		
			Role saveRole = roleRepository.save(role);
		
		if(saveRole!=null){
			
			List<RoleFeaturePermissionScheme> roleFeaturePermissionList = roleFeaturePermissionSchemeRepoitory.findByRole(role);
			
			for(RoleFeaturePermissionScheme  roleFeaturePermission : roleFeaturePermissionList ){
				
				roleFeaturePermission.setIsLive(false);
				
			}
			
			Iterable<RoleFeaturePermissionScheme> roleFeaturePermissionSave = roleFeaturePermissionSchemeRepoitory.save(roleFeaturePermissionList);
				
			return true;
			
		}else{
			throw new RuntimeException();
		}
		
		
		
		
	}

	

	@Override
	@Transactional
	public boolean enableRole(Integer roleId) {
		
		Role role = roleRepository.findByRoleId(roleId);
		
			role.setEnable(true);
		
			Role saveRole = roleRepository.save(role);
		
		if(saveRole!=null){
			
			List<RoleFeaturePermissionScheme> roleFeaturePermissionList = roleFeaturePermissionSchemeRepoitory.findByRole(role);
			
			for(RoleFeaturePermissionScheme  roleFeaturePermission : roleFeaturePermissionList ){
				
				roleFeaturePermission.setIsLive(true);
				
			}
			
			Iterable<RoleFeaturePermissionScheme> roleFeaturePermissionSave = roleFeaturePermissionSchemeRepoitory.save(roleFeaturePermissionList);
				
			return true;
			
		}else{
			throw new RuntimeException();
		}
		
		
		
		
	}
	
	@Override
	@Transactional
	public String deleteFeaturePermission(Integer roleFeaturePermissionId) {
		
		RoleFeaturePermissionScheme roleFeaturePermission = roleFeaturePermissionSchemeRepoitory.findByRoleFeaturePermissionSchemeId(roleFeaturePermissionId);

		if(false==roleFeaturePermission.getIsLive())
			return "already disabled";
		else{
			
			roleFeaturePermission.setIsLive(false);
			
			RoleFeaturePermissionScheme saveRoleFeaturePermission = roleFeaturePermissionSchemeRepoitory.save(roleFeaturePermission);
		
		
		if(saveRoleFeaturePermission!=null)
			return "successfull";
		else
			return "failure";
		}
		
	}

	@Override
	public String enableFeaturePermission(Integer roleFeaturePermissionId) {
		
	RoleFeaturePermissionScheme roleFeaturePermission = roleFeaturePermissionSchemeRepoitory.findByRoleFeaturePermissionSchemeId(roleFeaturePermissionId);
		
		if(true==roleFeaturePermission.getIsLive()){
			
			return "already enabled";
		}
		else			
				roleFeaturePermission.setIsLive(true);
		
		RoleFeaturePermissionScheme saveRoleFeaturePermission = roleFeaturePermissionSchemeRepoitory.save(roleFeaturePermission);
		
		if(saveRoleFeaturePermission!=null)
			return "successfull";
		else
			return "failure";
		
	}

	@Override
	@Transactional(readOnly=true)
	public Map<String,List<AreaModel>> getAllAreaList() {
		
		List<Area> areas = areaRepository.findAll();
		
		List<AreaModel> areaModelList = new ArrayList<>();
		
		Map<String,List<AreaModel>> areaMap = new LinkedHashMap<>();
		
		
		//setting areas is area-model list
		for(Area area : areas){
			
			AreaModel areaModel = new AreaModel();
			
			areaModel.setAreaCode(area.getAreaCode());
			areaModel.setAreaId(area.getAreaId());
			areaModel.setAreaLevel(area.getAreaLevel().getAreaLevelName());
			areaModel.setAreaName(area.getAreaName());
			areaModel.setCreatedDate(area.getCreatedDate());
			areaModel.setLive(area.isLive());
			areaModel.setParentAreaId(area.getParentAreaId());
			areaModel.setShortName(area.getShortName());
			areaModel.setUpdatedDate(area.getUpdatedDate());
				
			areaModelList.add(areaModel);
		}
		
		//making levelName as a key 
		for(AreaModel areaModel : areaModelList){
			
			
			if(areaMap.containsKey(areaModel.getAreaLevel())){
				
				areaMap.get(areaModel.getAreaLevel()).add(areaModel);
				
			}else{
				
				areaModelList = new ArrayList<>();
				areaModelList.add(areaModel);
				areaMap.put(areaModel.getAreaLevel(), areaModelList);
				
			}
			
			
		}
		return areaMap;
		
	}


	@Override
	@Transactional
	public boolean addNewFeaturePermission(Integer roleId, Integer featurePermissionId) {
		
		Role role = new Role(roleId);
		
		FeaturePermissionMapping featurePermissionMapping = new FeaturePermissionMapping(featurePermissionId);
		
		RoleFeaturePermissionScheme roleFeaturePermissionScheme = new RoleFeaturePermissionScheme();
		
		roleFeaturePermissionScheme.setFeaturePermissionMapping(featurePermissionMapping);
		roleFeaturePermissionScheme.setRole(role);
		roleFeaturePermissionScheme.setIsLive(true);
		
		RoleFeaturePermissionScheme roleFeaturePermission = roleFeaturePermissionSchemeRepoitory.save(roleFeaturePermissionScheme);
		
		if(roleFeaturePermission!=null){
			return true;
		}else{
			throw new RuntimeException();
		}
		
	}


	@Override
	@Transactional
	public ResponseEntity<Boolean> resetPassword(ResetPasswordModel resetPasswordModel) {
		
		User user = userRepository.findByUserId(resetPasswordModel.getUserId());
		
		user.setPassword(messageDigestPasswordEncoder.encodePassword(user.getUserName(), resetPasswordModel.getNewPassword()));
		
		User saveUser = userRepository.save(user);
		
		if(saveUser!=null){
			return new ResponseEntity<Boolean>(true,HttpStatus.OK);
		}
		return new ResponseEntity<Boolean>(false,HttpStatus.CONFLICT);
	}


	//please read message from properties file
	@Override
	@Transactional
	public ResponseEntity<String> changePassoword(ChangePasswordModel changePasswordModel) {

		//using it to parse string as json
		Gson gson = new Gson();
		
		// check newpassword is same as confirm password
		if (changePasswordModel.getNewPassword().equals(changePasswordModel.getConfirmPassword())) {

			User user = userRepository.findByUserName(changePasswordModel.getUserName());

			// check user has entered correct old password or not
			if (user.getPassword().equals(messageDigestPasswordEncoder.encodePassword(user.getUserName(),changePasswordModel.getOldPassword()))) {

				// check new password is same as db password or not if same than return
				if (!user.getPassword().equals(messageDigestPasswordEncoder.encodePassword(user.getUserName(),
						changePasswordModel.getNewPassword()))) {

					user.setPassword(messageDigestPasswordEncoder.encodePassword(user.getUserName(),changePasswordModel.getNewPassword()));

					User saveUser = userRepository.save(user);

					if (saveUser != null) {

						return new ResponseEntity<>(gson.toJson(messageSource.getMessage("password.update.success", null,null)), HttpStatus.OK);

					} else {

						return new ResponseEntity<>(gson.toJson(messageSource.getMessage("password.update.failure", null,null)), HttpStatus.CONFLICT);
					}

				} else {
					return new ResponseEntity<>(gson.toJson(messageSource.getMessage("new.password.previous.password", null,null)), HttpStatus.FORBIDDEN);
				}

			} else {

				return new ResponseEntity<>(gson.toJson(messageSource.getMessage("password.not.matching", null,null)), HttpStatus.FORBIDDEN);
			}

		} else
			return new ResponseEntity<>(gson.toJson(messageSource.getMessage("new.password.confirm.password.not.matching", null,null)), HttpStatus.FORBIDDEN);

	}


	@Override
	@Transactional
	public ResponseEntity<String> forgotPassword(String userName,String otp) {
		
		//using it to parse string as json
		Gson gson = new Gson();
		
		User user = userRepository.getUserByUserNameOrEmail(userName);
		
		//check otp is same or not
		ResponseEntity<String> validationResulut = validateOtp(userName,otp);
		
		if(validationResulut.getStatusCodeValue()!=200){
			
			return validationResulut;
			
		}
		
			if(user!=null &&user.isEnabled()){
			
			String newPassword = RandomStringUtils.randomAlphanumeric(6);
			
			user.setPassword(messageDigestPasswordEncoder.encodePassword(user.getUserName(), newPassword));
			
			User saveUser =userRepository.save(user);
			
			if(saveUser!=null){
				
				//send mail
				Mail mail = new Mail();

				List<String> emailId=new ArrayList<String>();
				emailId.add(user.getEmail());
				
				mail.setToEmailIds(emailId);
				mail.setToUserName(user.getName());
				mail.setSubject("Forgot Password");
				
				//check in property file wheter this key is available or not
				//ConfigurableEnvironment allow us to that if return true available 
				if(!configurableEnvironment.containsProperty("user.forgot.password")){
					
					return new ResponseEntity<>(gson.toJson("couldn't find user.forgot.password key in property file "), HttpStatus.FORBIDDEN);
				}
				
				mail.setMessage(messageSource.getMessage("user.forgot.password", null,null)+newPassword);
				mail.setFromUserName("Administrator");
				
				String result = mailService.sendMail(mail);
				
				if(result.equals("error")){
					
					return new ResponseEntity<>(gson.toJson("couldn't find authentication.userid OR authentication.password key in property file "), HttpStatus.FORBIDDEN);
					
				}else if(result.equals("Done")){
					
					return new ResponseEntity<>(gson.toJson(messageSource.getMessage("password.forgot.success", null,null)), HttpStatus.OK);
				}else{
					return new ResponseEntity<>(gson.toJson("error"), HttpStatus.CONFLICT);
				}
				
			}else{
				return new ResponseEntity<>(gson.toJson("error"), HttpStatus.CONFLICT);
			}
			
			
		}else
			return new ResponseEntity<>(gson.toJson(messageSource.getMessage("password.reset.failure", null,null)), HttpStatus.FORBIDDEN);
	}


	@Override
	@Transactional
	public ResponseEntity<String> sendOtp(String userName) {
		
		//using it to parse string as json
		Gson gson = new Gson();
		
		User user = userRepository.getUserByUserNameOrEmail(userName);
		
		if(user==null)
			return new ResponseEntity<>(gson.toJson(messageSource.getMessage("user.not.found", null,null)),HttpStatus.NOT_FOUND);
		
		//generate otp
		Random random = new Random();

		//6 DIGIT otp
		String otp = String.format("%04d", random.nextInt(10000));
		
		user.setOtp(otp);
		user.setOtpGeneratedDateTime(new Date());
		user.setInvalidAttempts((short) 0);
		
		User saveUser = userRepository.save(user);
		
		if(saveUser!=null){
			//send mail
			
			Mail mail = new Mail();
			
			List<String> emailId=new ArrayList<String>();
			emailId.add(user.getEmail());
			
			mail.setToEmailIds(emailId);
			mail.setToUserName(user.getName());
			mail.setSubject("Forgot Password OTP");
			
			mail.setMessage(messageSource.getMessage("otp.send.message", null,null)+otp);
			mail.setFromUserName("Administrator");
			
			String result = mailService.sendMail(mail);
			
			if(result.equals("error")){
				
				return new ResponseEntity<>(gson.toJson("couldn't find authentication.userid OR authentication.password key in property file "), HttpStatus.FORBIDDEN);
				
			}else if(result.equals("Done")){
				
				return new ResponseEntity<>(gson.toJson(messageSource.getMessage("otp.sent.success", null,null)), HttpStatus.OK);
			}else{
				return new ResponseEntity<>(gson.toJson("error"), HttpStatus.CONFLICT);
			}
			
			
		}else{
			
			return new ResponseEntity<>(gson.toJson("error"),HttpStatus.CONFLICT);
			
		}
		
	}


	@Override
	@Transactional
	public ResponseEntity<String> validateOtp(String username, String otp) {
		
		//using it to parse string as json
		Gson gson = new Gson();
		
		User user = userRepository.getUserByUserNameOrEmail(username);
		
		if ((user.getOtpGeneratedDateTime().getTime() + 10 * 60000) <= new Date().getTime()) {
			
			return new ResponseEntity<>(gson.toJson("Password reset time expired !"),HttpStatus.BAD_REQUEST);
			
		}
		if (user.getInvalidAttempts() == 10) {
			
			return new ResponseEntity<>(gson.toJson("Not allowed! No of attempts exceeded limit.i.e " + 10),HttpStatus.BAD_REQUEST);
		}

		if (user.getOtp().equals(otp)) {
			
			return new ResponseEntity<>(gson.toJson("Otp Validated !"),HttpStatus.OK);
			
		} else {
			
			user.setInvalidAttempts((short) (user.getInvalidAttempts() + 1));
			
			if(10 - user.getInvalidAttempts()!=0){
				
				return new ResponseEntity<>( gson.toJson("Invalid OTP! You have " + (10 - user.getInvalidAttempts())+ " available attempts"),HttpStatus.BAD_REQUEST);
			}else{
				return new ResponseEntity<>( gson.toJson("Invalid OTP ! No of attempts exceeded limit.i.e " +10),HttpStatus.BAD_REQUEST);
			}
			
			
		}
		
		
	}


}
