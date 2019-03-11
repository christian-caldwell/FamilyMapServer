package Service.Person;

import Service.Template.Request;

public class PersonRequest extends Request{
	private String token;
	private String[] parameters;
	
	public PersonRequest(String token, String[] parameters) {
		this.token = token;
		this.parameters = parameters;
	}
	
	public String getToken() {
		return this.token;
	}
	
	public String[] getParameters() {
		return this.parameters;
	}

}
