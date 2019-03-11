package Service.Load;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.sun.net.httpserver.*;

import Model.User;
import Model.Person;
import Model.Event;
import Server.MainServer;
import Server.ServerOutput;
import Server.ServerInput;
import Service.Template.Handler;

public class LoadHandler extends Handler implements HttpHandler  {
	private ServerInput input;
	private Gson gson = new Gson();
	private ArrayList<User> userList;
	private ArrayList<Person> personList;
	private ArrayList<Event> eventList;
	
	public LoadHandler (MainServer server, ServerOutput output, ServerInput input) {
		super(server, output);
		this.input = input;
	}
	
	@Override
	public void handle(HttpExchange httpExchange) {

		printInfo(httpExchange, "Load");
		this.userList = new ArrayList<User>();
		this.personList = new ArrayList<Person>();
		this.eventList = new ArrayList<Event>();
		
		try
		{
			InputStream body=httpExchange.getRequestBody();
			String bodyParts= input.streamToString(body);
			System.out.println("    Response Body: " + bodyParts);		
			JsonObject json = gson.fromJson(bodyParts, JsonObject.class);
			JsonArray jsonUserArray = json.getAsJsonArray("users");

			for (JsonElement currJson :jsonUserArray) {

				if (!currJson.isJsonNull() && currJson.isJsonObject()) {
					JsonObject currObject = (JsonObject)currJson;

					User currUser = new User();
					currUser.setUserName(currObject.get("userName").getAsString());
					currUser.setGender(currObject.get("gender").getAsString().toLowerCase());
					currUser.setPassword(currObject.get("password").getAsString());
					currUser.setEmail(currObject.get("email").getAsString());

					currUser.setFirstName(currObject.get("firstName").getAsString());
					currUser.setLastName(currObject.get("lastName").getAsString());
					currUser.setPersonId(currObject.get("personID").getAsString());

					this.userList.add(currUser);

				}
			}

			JsonArray jsonPersonArray = json.getAsJsonArray("persons");
			for (JsonElement currJson: jsonPersonArray) {

				if (currJson.isJsonObject()) {
					JsonObject currObject = (JsonObject)currJson;
					Person currPerson = new Person();

					currPerson.setDescendant(currObject.get("descendant").getAsString());
					currPerson.setPersonID(currObject.get("personID").getAsString());
					currPerson.setFirstName(currObject.get("firstName").getAsString());
					currPerson.setLastName(currObject.get("lastName").getAsString());
					currPerson.setGender(currObject.get("gender").getAsString());

					if (currObject.has("father")) {
						currPerson.setFather(currObject.get("father").getAsString());
					}
					if (currObject.has("mother")) {
						currPerson.setMother(currObject.get("mother").getAsString());
					}
					if (currObject.has("spouse")) {
						currPerson.setSpouse(currObject.get("spouse").getAsString());
					}
					this.personList.add(currPerson);
				}
			}


			JsonArray jsonEventArray = json.getAsJsonArray("events");
			for (JsonElement currJson: jsonEventArray) {
				if (currJson.isJsonObject()) {

					JsonObject currObject = (JsonObject)currJson;
					Event currEvent = new Event();
					currEvent.setDescendant(currObject.get("descendant").getAsString());
					currEvent.setEventID(currObject.get("eventID").getAsString());
					currEvent.setPersonID(currObject.get("personID").getAsString());
					currEvent.setLatitude(currObject.get("latitude").getAsDouble());
					currEvent.setLongitude(currObject.get("longitude").getAsDouble());
					currEvent.setCountry(currObject.get("country").getAsString());
					currEvent.setCity(currObject.get("city").getAsString());
					currEvent.setDescription(currObject.get("eventType").getAsString());

					currEvent.setYear(currObject.get("year").getAsInt());
					this.eventList.add(currEvent);


				}
			}

		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("    Error with stream " + e.getMessage());
		}
		
		LoadRequest request = new LoadRequest(this.userList, this.personList, this.eventList);
		LoadProvider provider = new LoadProvider();
		LoadResponse response = provider.execute(request);
		this.output.createAndSend(response.getReportErrorMessage(), httpExchange);
		
	}
}
