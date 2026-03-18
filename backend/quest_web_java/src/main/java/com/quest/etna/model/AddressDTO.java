package com.quest.etna.model;

public class AddressDTO {
	private String message;

	public void setMessage(String message) {
		this.message = message;
	}

	public AddressDTO(String message) {
		super();
		this.message = message;
	}
	
	public void setForbiddenMessage() {
		this.message = "You are not authorized to modify an address not belonging to you.";
	}
	
	public void setNotFoundMessage() {
		this.message = "The address you're looking for does not exist.";
	}
	
	public void setErrorMessage() {
		this.message = "An error has been occured.";
	}

	public String getMessage() {
		return message;
	}
}
