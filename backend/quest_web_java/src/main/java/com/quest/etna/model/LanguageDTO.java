package com.quest.etna.model;

public class LanguageDTO {
	private String message;

	public void setMessage(String message) {
		this.message = message;
	}

	public LanguageDTO(String message) {
		super();
		this.message = message;
	}
	
	public void setForbiddenMessage() {
		this.message = "You are not authorized to modify a language.";
	}
	
	public void setNotFoundMessage() {
		this.message = "The language you're looking for does not exist.";
	}
	
	public void setErrorMessage() {
		this.message = "An error has been occured.";
	}

	public String getMessage() {
		return message;
	}
}
