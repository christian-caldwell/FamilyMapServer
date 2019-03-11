package Service.Load;

import java.util.ArrayList;

import Model.Event;
import Model.Person;
import Model.User;
import Service.Template.Request;

public class LoadRequest extends Request{
	private ArrayList<User> userList;
	private ArrayList<Person> personList;
	private ArrayList<Event> eventList;
	
	public LoadRequest(ArrayList<User> userList, ArrayList<Person> personList, ArrayList<Event> eventList) {
		this.userList = userList;
		this.personList = personList;
		this.eventList = eventList;
	}

	public ArrayList<User> getUserList() {
		return this.userList;
	}
	
	public ArrayList<Person> getPersonList() {
		return this.personList;
	}
	
	public ArrayList<Event> getEventList() {
		return this.eventList;
	}
}
