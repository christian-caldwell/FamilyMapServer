package Service.Login;

import Service.Template.Response;
import Model.AuthToken;

public class LoginResponse extends Response {
	private AuthToken token; 
	
	public LoginResponse(String message, boolean status) {
		super(message, status);
	}
	
	public LoginResponse(AuthToken token, boolean status) {
		super(null, status);
		this.token = token;
	}
	
	public AuthToken getToken() {
		return this.token;
	}

}
