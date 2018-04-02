package com.example.controller;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.RoleFeaturePermissionScheme;
import com.example.domain.UserAreaMapping;
import com.example.domain.UserModel;
import com.example.domain.UserRoleFeaturePermissionMapping;

@RestController
@RequestMapping("/api")
public class WebController {

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ConfigurableEnvironment  configurableEnvironment;///it checks the value in propoerty file

	@RequestMapping(value="/testing") 
	//@ResponseBody
	 @Produces({"application/json;response-pass-through=true"})
	public String hello() 
	{ 
	
		return "hello"; 
	}

	// hasRole not working instead that use hasAuthority
	@PreAuthorize("hasAuthority('Administrator:Setting Feature,Edit')")
	@RequestMapping(value = "/test")
	@ResponseBody
	public Integer helloWorld() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object myUser = auth.getPrincipal();

		/* if(myUser instanceof UserModel) */
		Integer role = null;
		UserModel user = (UserModel) myUser;

		Collection<UserAreaMapping> areaMappings = user.getAreaMappings();

		for (UserAreaMapping userAreaMapping : areaMappings) {

			List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappingList = userAreaMapping.getUserRoleFeaturePermissionMappings();

			for (UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping : userRoleFeaturePermissionMappingList) {
				
				RoleFeaturePermissionScheme roleFeaturePermissionScheme = userRoleFeaturePermissionMapping.getRoleFeaturePermissionScheme();
				
				role = roleFeaturePermissionScheme.getRole().getRoleId();
				break;

			}
		}
		
		return role;

	}

	@RequestMapping(value="/check")
	@ResponseBody
	public String helloworld(){
		
		String message =  messageSource.getMessage("user.hello", null,null);
		
		boolean result = configurableEnvironment.containsProperty("user.hello");
		
		boolean result1 = configurableEnvironment.containsProperty("spring.jpa.show-sql");
		
		if(message!=null){
			return message;
		}
		return null;
	}
	
	
	
}
