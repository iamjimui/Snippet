package com.quest.etna.model;

public class CommentDTO {
	private Integer id;
	private String message;
	private String username;
	private Integer snippet_id;

	public void setMessage(String message) {
		this.message = message;
	}

	public CommentDTO(String message) {
		super();
		this.message = message;
	}
	
	public CommentDTO(Integer id, String message, String username, Integer snippet_id) {
		this.id = id;
		this.message = message;
		this.username = username;
		this.snippet_id = snippet_id;
	}
	
	public void setForbiddenMessage() {
		this.message = "You are not authorized to modify a comment not belonging to you.";
	}
	
	public void setNotFoundMessage() {
		this.message = "The comment you're looking for does not exist.";
	}
	
	public void setErrorMessage() {
		this.message = "An error has been occured.";
	}

	public String getMessage() {
		return message;
	}
}
