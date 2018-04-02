package com.example.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user_tbl")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6142358483948073924L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;

	@Column
	private String name;

	@Column(name = "user_name", unique = true)
	private String userName;

	private String password;

	@Column(name = "enabled", columnDefinition = "boolean DEFAULT true")
	private boolean enabled = true;

	@Column(name = "credentialexpired", columnDefinition = "boolean DEFAULT false")
	private boolean credentialexpired = false;

	@Column(name = "expired", columnDefinition = "boolean DEFAULT false")
	private boolean expired;

	@Column(name = "locked", columnDefinition = "boolean DEFAULT false")
	private boolean locked = false;

	@Column(name = "email_id")
	private String email;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	Collection<UserAreaMapping> areas;

	@Column
	private String otp;

	@Column(name = "invalid_attempts", columnDefinition = "smallint default '0'")
	private short invalidAttempts;

	@Column(name = "last_otp_generated_time")
	private Date otpGeneratedDateTime;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isCredentialexpired() {
		return credentialexpired;
	}

	public void setCredentialexpired(boolean credentialexpired) {
		this.credentialexpired = credentialexpired;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public Collection<UserAreaMapping> getAreas() {
		return areas;
	}

	public void setAreas(Collection<UserAreaMapping> areas) {
		this.areas = areas;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public User() {
		super();
	}

	public User(Integer id) {
		this.userId = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public short getInvalidAttempts() {
		return invalidAttempts;
	}

	public void setInvalidAttempts(short invalidAttempts) {
		this.invalidAttempts = invalidAttempts;
	}

	public Date getOtpGeneratedDateTime() {
		return otpGeneratedDateTime;
	}

	public void setOtpGeneratedDateTime(Date otpGeneratedDateTime) {
		this.otpGeneratedDateTime = otpGeneratedDateTime;
	}

}
