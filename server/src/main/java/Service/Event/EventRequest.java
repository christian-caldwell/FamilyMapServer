package Service.Event;

import Service.Template.Request;

public class EventRequest extends Request{
	private String token;
	private String[] parameters;

	public EventRequest(String token, String[] parameters) {
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
