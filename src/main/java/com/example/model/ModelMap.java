package com.example.model;

import com.example.domain.FeaturePermissionMapping;

public class ModelMap {
	
	private FeaturePermissionMapping featurePermission;
	
	private boolean isLive;
	
	private boolean isEnable;
	

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	public FeaturePermissionMapping getFeaturePermission() {
		return featurePermission;
	}

	public void setFeaturePermission(FeaturePermissionMapping featurePermission) {
		this.featurePermission = featurePermission;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	
	

}
