package Service.Fill;

import Service.Template.Request;

public class FillRequest extends Request{
	private String username;
	private int levels;
	
	public FillRequest(String username, int levels) {
		this.username = username;
		this.levels = levels;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public int getLevels() {
		return this.levels;
	}
}
