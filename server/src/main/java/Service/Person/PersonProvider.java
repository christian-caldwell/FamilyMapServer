package Service.Person;

import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import Data.DataBase;
import Model.Person;
import Model.Report;
import Model.User;
import Service.Template.Provider;
import Data.Helpers.AuthHelper;
import Data.Helpers.UserHelper;
import Data.Helpers.PersonHelper;


public class PersonProvider  extends Provider {
	private Gson gson = new Gson();
	
	public PersonResponse execute(PersonRequest request) {
		String givenToken = request.getToken();
		String[] givenParameters = request.getParameters();

		if (AuthHelper.authenticateToken(this.db, givenToken) != null)
		{
			User user = UserHelper.getUserByAccessToken(this.db, givenToken);
			if(givenParameters.length <= 2)
			{
				List<Person> persons = UserHelper.getUserNamesFamily(this.db, user.getUserName());
				if(persons != null)
				{
					JsonObject json = new JsonObject();
					json.add("data", gson.toJsonTree(persons));
					this.processSuccessful = true;
					return new PersonResponse(json, processSuccessful);
				}
				else {
					this.processSuccessful = false;
					this.responseString = "Database Error";
					return new PersonResponse(responseString, processSuccessful);
				}
			}
			else if (givenParameters.length == 3)
			{
				Person person = PersonHelper.getPersonByID(this.db, givenParameters[2], user.getUserName());
				if(person == null) {
					this.processSuccessful = false;
					this.responseString = "Incorrect AuthToken or PersonID, Check ID and Refresh Token.";
					return new PersonResponse(responseString, processSuccessful);
				}
				else {
					this.processSuccessful = true;
					return new PersonResponse(person, processSuccessful);
				}
			}
			else {
				this.processSuccessful = false;
				this.responseString ="Incorrect Number of Parameters, Check URI";
				return new PersonResponse(responseString, processSuccessful);
			}
		}
		else {
			this.processSuccessful = false;
			this.responseString = "Authtoken Incorrect - Not authenticated or missing.";

			return new PersonResponse(responseString, processSuccessful);
		}
	}
}
