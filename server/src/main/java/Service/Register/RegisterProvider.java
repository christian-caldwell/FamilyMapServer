package Service.Register;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import Data.DataBase;
import Data.Helpers.PersonHelper;
import Model.AuthToken;
import Model.Person;
import Model.Report;
import Model.User;
import Service.Fill.FillResponse;
import Service.Login.LoginResponse;
import Service.Template.Provider;
import Data.Helpers.AuthHelper;
import Data.Helpers.ClearHelper;
import Data.Helpers.FillHelper;
import Data.Helpers.UserHelper;

public class RegisterProvider  extends Provider {
	private Gson gson = new Gson();
	JsonArray fNamesArray;
	JsonArray mNamesArray;
	JsonArray sNamesArray;
	JsonArray locationsArray;

	final private int GENERATION_LENGTH = 30;
	final private int CURRENT_YEAR = 2018;
	

	public RegisterResponse execute(RegisterRequest request) {
		User user = request.getUser();
		if(UserHelper.getUserByUsername(this.db, user.getUserName()) != null)
		{
			this.responseString = "User name already taken. Try a different user name";
			this.processSuccessful = false;
			return new RegisterResponse(this.responseString, processSuccessful);
		}
		else if(UserHelper.registerUser(this.db, user))
		{
			String fnames = "data" + File.separator + "fnames.json";
			String locations = "data" + File.separator + "locations.json";
			String mnames = "data" + File.separator + "mnames.json";
			String snames = "data" + File.separator + "snames.json";
			
			fNamesArray = readData(fnames);
			mNamesArray = readData(mnames);
			sNamesArray = readData(snames);
			locationsArray = readData(locations);
			
			int birthDate = this.CURRENT_YEAR - this.GENERATION_LENGTH;
			
			ClearHelper.clearUser(this.db, request.getUser().getUserName());
			FillHelper.resetFields(this.db, fNamesArray, mNamesArray, sNamesArray, locationsArray);
			FillHelper.beginFill();

			Person filledUser= UserHelper.getUserPerson();
			Report report = FillHelper.createFamily(filledUser, birthDate, request.getGenerations(), request.getUser().getUserName());
			if (!report.getStatus()) {
				FillHelper.endFill(report.getStatus());
				return new RegisterResponse("There was an error loading the DB. Error message: " + report.getMessage(),report.getStatus());
				
			}


			report = FillHelper.createEvents(filledUser, birthDate, request.getGenerations(), request.getUser().getUserName());
			if (!report.getStatus()) {
				FillHelper.endFill(report.getStatus());
				return new RegisterResponse("There was an error loading the DB. Error message: " + report.getMessage(),report.getStatus());
				
			}
			
			FillHelper.endFill(true);
			int personCount = FillHelper.getPersonCount();
			int eventCount = FillHelper.getEventCount();
			
			user = UserHelper.getUserByUsername(this.db, user.getUserName());
			AuthHelper.confirmPassword(db, user);
			AuthToken token = AuthHelper.getNewToken();
			System.out.println(token.getPersonID());
			this.responseString = null;
			this.processSuccessful = true;
			return new RegisterResponse(token, processSuccessful);
		}
		else {
			this.responseString = "Error registering the user";
			this.processSuccessful = false;
			return new RegisterResponse(this.responseString, processSuccessful);
			
		}
	}
	
	private JsonArray readData(String fnames)
	{
		try
		{
			InputStreamReader in = new InputStreamReader(new FileInputStream(fnames), "UTF8");
	        BufferedReader reader = new BufferedReader(in);
	        StringBuilder out = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            out.append(line);
	        }
	        reader.close();

			
			try
			{
			JsonObject fnamesJson = gson.fromJson(out.toString(), JsonObject.class);
			
			return fnamesJson.getAsJsonArray("data");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
