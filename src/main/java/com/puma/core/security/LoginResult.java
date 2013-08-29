package com.puma.core.security;

public class LoginResult {
	
	private boolean logged = false;
	
	private String message = "";
	
	public LoginResult(boolean isSuccess){
		this.logged = isSuccess;
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

	
}
