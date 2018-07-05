package fr.projectfactory.api.model.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PasswordChangeRequest {

	@NotNull
	private String oldPassword;

	@NotNull
	private String newPassword;

}
