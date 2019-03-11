package Service.Event;

import java.sql.SQLException;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.Gson;

import Data.DataBase;
import Data.Helpers.EventHelper;
import Model.Event;
import Model.Report;
import Model.User;
import Service.Person.PersonResponse;
import Service.Template.Provider;
import Data.Helpers.AuthHelper;
import Data.Helpers.UserHelper;

public class EventProvider  extends Provider {
	private Gson gson = new Gson();

	public EventResponse execute(EventRequest request) {
		String givenToken = request.getToken();
		String[] givenParameters = request.getParameters();

		
		if(AuthHelper.authenticateToken(this.db, givenToken) != null)
		{
			User user = UserHelper.getUserByAccessToken(this.db, givenToken);
			if(givenParameters.length == 3)
			{
				Event event = EventHelper.getEventByID(this.db, givenParameters[2], user.getUserName());
				if(event == null) {
					this.responseString = "No Event Found, Check ID Number Generate New Authtoken";
					this.processSuccessful = false;
					return new EventResponse(this.responseString, processSuccessful);
				}
				else {
					this.responseString = null;
					this.processSuccessful = false;
					return new EventResponse(event, processSuccessful);
				}
			}
			else if (givenParameters.length > 3)
			{
				this.responseString = "URI Incorrect, Too Many Params.";
				this.processSuccessful = false;
				return new EventResponse(this.responseString, processSuccessful);
			}
			else
			{
				List<Event> events = EventHelper.getAllEventsFromFamilyByUser(this.db, user.getUserName());
				if(events == null) {
					this.responseString = "Database Error.";
					this.processSuccessful = false;
					return new EventResponse(this.responseString, processSuccessful);
					}
				else
				{
					JsonObject json = new JsonObject();
					json.add("data", gson.toJsonTree(events));
					this.responseString = null;
					this.processSuccessful = false;
					return new EventResponse(json, processSuccessful);
				}
			}
		}
		else {
			this.responseString = "Authtoken Incorrect - Not authenticated or missing.";
			this.processSuccessful = false;
			return new EventResponse(this.responseString, processSuccessful);
		}
	}
}
