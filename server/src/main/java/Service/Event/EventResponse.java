package Service.Event;

import com.google.gson.JsonObject;

import Model.Event;
import Service.Template.Response;

public class EventResponse extends Response {
	JsonObject json;
	Event event; 
	
	public EventResponse(String message, boolean status) {
		super(message, status);
	}
	
	public EventResponse(JsonObject json, boolean status) {
		super(null, status);
		this.json = json;
	}
	
	public EventResponse(Event event, boolean status) {
		super(null, status);
		this.event = event;
	}
	
	public JsonObject getJson() {
		return this.json;
	}
	
	public Event getEvent() {
		return this.event;
	}

}
