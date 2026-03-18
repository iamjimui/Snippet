package com.quest.etna.model;

public class SnippetDTO {
	private String message;

	public void setMessage(String message) {
		this.message = message;
	}

	public SnippetDTO(String message) {
		super();
		this.message = message;
	}
	
	public void setForbiddenMessage() {
		this.message = "You are not authorized to modify a snippet not belonging to you.";
	}
	
	public void setNotFoundMessage() {
		this.message = "The snippet you're looking for does not exist.";
	}
	
	public void setErrorMessage() {
		this.message = "An error has been occured.";
	}

	public String getMessage() {
		return message;
	}
}
