package Service.Register;

import Service.Template.Request;
import Model.User;

public class RegisterRequest extends Request{
	private int generations;
	private User user;
	
	RegisterRequest(User user, int generations) {
		this.generations = generations;
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}
	
	public int getGenerations() {
		return this.generations;
	}
}
