package com.cgi.projectfactory.api.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PasswordChange {

	@NotNull
	private String oldPassword;
	
	@NotNull
	private String newPassword;

}
