package com.quest.etna.model;

public class TagDTO {
	private String message;

	public void setMessage(String message) {
		this.message = message;
	}

	public TagDTO(String message) {
		super();
		this.message = message;
	}
	
	public void setForbiddenMessage() {
		this.message = "You are not authorized to modify a tag.";
	}
	
	public void setNotFoundMessage() {
		this.message = "The tag you're looking for does not exist.";
	}
	
	public void setErrorMessage() {
		this.message = "An error has been occured.";
	}

	public String getMessage() {
		return message;
	}
}
