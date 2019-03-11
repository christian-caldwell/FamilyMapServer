package Service.Login;

import Service.Template.Request;
import Model.User;

public class LoginRequest extends Request{
	private User user;
	public LoginRequest(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return this.user;
	}
}
