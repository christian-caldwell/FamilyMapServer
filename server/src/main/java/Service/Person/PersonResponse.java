package Service.Person;

import Service.Template.Response;
import Model.Person;
import com.google.gson.JsonObject;

public class PersonResponse extends Response {
	JsonObject json;
	Person person; 
	
	public PersonResponse(String message, boolean status) {
		super(message, status);
	}
	
	public PersonResponse(JsonObject json, boolean status) {
		super(null, status);
		this.json = json;
	}
	
	public PersonResponse(Person person, boolean status) {
		super(null, status);
		this.person = person;
	}
	
	public JsonObject getJson() {
		return this.json;
	}
	
	public Person getPerson() {
		return this.person;
	}

}
