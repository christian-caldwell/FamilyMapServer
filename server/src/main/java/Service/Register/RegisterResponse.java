package Service.Register;

import Service.Template.Response;
import Model.AuthToken;

public class RegisterResponse extends Response {
	private AuthToken token;
	
	public RegisterResponse(String message, boolean status) {
		super(message, status);
	}
	
	public RegisterResponse(AuthToken token, boolean status) {
		super(null, status);
		this.token = token;
	}
	
	public AuthToken getToken() {
		return this.token;
	}

}
