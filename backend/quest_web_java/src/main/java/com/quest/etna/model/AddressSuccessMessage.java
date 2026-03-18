package com.quest.etna.model;

public class AddressSuccessMessage {
	private Boolean success;
	
	public AddressSuccessMessage(Boolean success) {
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
