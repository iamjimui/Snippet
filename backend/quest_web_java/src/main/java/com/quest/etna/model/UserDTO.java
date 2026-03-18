package com.quest.etna.model;

public class UserDTO {
	private String message;

	public void setMessage(String message) {
		this.message = message;
	}

	public UserDTO(String message) {
		super();
		this.message = message;
	}
	
	public void setDuplicataMessage() {
		this.message = "User already exists !";
	}
	
	public void setForbiddenMessage() {
		this.message = "You are not an Admin.";
	}
	
	public void setErrorMessage() {
		this.message = "An error has been occured.";
	}
	
	public void setNotFoundMessage() {
		this.message = "The user you're looking for does not exist.";
	}

	public String getMessage() {
		return message;
	}
}
