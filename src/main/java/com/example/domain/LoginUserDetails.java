package com.example.domain;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="login_user_details")
public class LoginUserDetails {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="login_meta_id_pk")
	private Integer loginMetaId;
	
	@Column(name="ip_address")
	private String ipAddress;
	
	@Column(name="user_agent")
	private String browser;
	
	@ManyToOne
	@JoinColumn(name="user_fk")
	private User user;
	
	@CreationTimestamp
	@Column(name="login_time")
	private Timestamp loginTime;
	
	@Column(name="logout_time")
	private Timestamp logoutTime;
	

	public Integer getLoginMetaId() {
		return loginMetaId;
	}

	public void setLoginMetaId(Integer loginMetaId) {
		this.loginMetaId = loginMetaId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	public Timestamp getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Timestamp logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public LoginUserDetails() {
		super();
	}
	
	public LoginUserDetails(Integer id) {
		this.loginMetaId = id;
	}
	
}
