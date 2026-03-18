package com.quest.etna.model;

public class UserSuccessMessage {
	private Boolean success;
	
	public UserSuccessMessage(Boolean success) {
		super();
		this.success = success;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
}
