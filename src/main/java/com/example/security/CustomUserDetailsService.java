package com.example.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.RoleFeaturePermissionScheme;
import com.example.domain.User;
import com.example.domain.UserAreaMapping;
import com.example.domain.UserModel;
import com.example.domain.UserRoleFeaturePermissionMapping;
import com.example.exception.UserNotFoundException;
import com.example.service.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Autowired
	private UserService userService;


	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 * 
	 *  @PreAuthorize("hasAuthority('Administrator:Setting Feature,Edit')")
	 *  
	 *  we can also add based on only feature and permission as per need
	 */
	@Override
	@Transactional
	public UserModel loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		User user = userService.findByUsername(userName);

		if (user == null) {
			
			throw new UserNotFoundException("user Not found");
			
		}

		
		Collection<UserAreaMapping> areaMappings = user.getAreas();
		
		List<UserRoleFeaturePermissionMapping> list = new ArrayList<>();
		
		//add those rolefeaturepermission where islive is true to areaMappings
		for(UserAreaMapping userArea :areaMappings ){
			
			List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappingsList= userArea.getUserRoleFeaturePermissionMappings();
			
			for(UserRoleFeaturePermissionMapping userRole : userRoleFeaturePermissionMappingsList){
				
				if(userRole.getRoleFeaturePermissionScheme().getIsLive()==true){
					
					list.add(userRole);
					
				}
				
			}
			
		}
		
		/*Collection<UserAreaMapping> areaMapping = areaMappings.stream().filter(a-> a.getUserRoleFeaturePermissionMappings().stream()
				.filter(v-> v.getRoleFeaturePermissionScheme().getIsLive()==true)).collect(Collectors.toList());*/
		
		
		for(UserAreaMapping area :areaMappings ){
			
			if(!area.getUserRoleFeaturePermissionMappings().isEmpty()){
				
				area.getUserRoleFeaturePermissionMappings().removeAll(area.getUserRoleFeaturePermissionMappings());
			
				area.setUserRoleFeaturePermissionMappings(list);
			}
		}
		//END
		
	
		
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

		for (UserAreaMapping userAreaMapping : areaMappings) {
			
			List<UserRoleFeaturePermissionMapping> featureAndPermissions = userAreaMapping.getUserRoleFeaturePermissionMappings();
			
			for (UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping : featureAndPermissions) {
				
				RoleFeaturePermissionScheme roleFeaturePermissionScheme = userRoleFeaturePermissionMapping.getRoleFeaturePermissionScheme();
				
				grantedAuthorities.add(new SimpleGrantedAuthority(
						
						roleFeaturePermissionScheme.getRole().getRoleName().concat(":").
						
						concat(roleFeaturePermissionScheme.getFeaturePermissionMapping().getFeature().getFeatureName()).concat(",")
						
						.concat(roleFeaturePermissionScheme.getFeaturePermissionMapping().getPermission().getPermissionName())));
				
				
				logger.info("\n<Granted Authorities To User> , {} \n",roleFeaturePermissionScheme.getRole().getRoleName().concat(":").
							concat(roleFeaturePermissionScheme.getFeaturePermissionMapping().getFeature().getFeatureName())
							.concat(",")
							.concat
							(roleFeaturePermissionScheme.getFeaturePermissionMapping().getPermission().getPermissionName()));

			}
		}
		return new UserModel(user.getUserName(), user.getPassword(), user.isEnabled(), !user.isExpired(), !user.isCredentialexpired(), !user.isLocked(), grantedAuthorities, user.getUserId(), areaMappings);
	}

}
