package Service.Fill;

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
import Model.Report;
import Service.Template.Provider;
import Data.Helpers.*;
import Model.*;
public class FillProvider  extends Provider {
	Gson gson = new Gson();
	JsonArray fNamesArray;
	JsonArray mNamesArray;
	JsonArray sNamesArray;
	JsonArray locationsArray;

	final private int GENERATION_LENGTH = 30;
	final private int CURRENT_YEAR = 2018;
	
	public FillResponse execute(FillRequest request) {
		
		User prevFilledUser = UserHelper.getUserByUsername(db, request.getUsername());
		if (prevFilledUser == null)
		{
			return new FillResponse("The supplied user is not yet " +
					"registered. Please register the user first", false);
		}
		else {
			String fnames = "data" + File.separator + "fnames.json";
			String locations = "data" + File.separator + "locations.json";
			String mnames = "data" + File.separator + "mnames.json";
			String snames = "data" + File.separator + "snames.json";

			this.fNamesArray = readData(fnames);
			this.mNamesArray = readData(mnames);
			this.sNamesArray = readData(snames);
			this.locationsArray = readData(locations);
			
			int birthDate = this.CURRENT_YEAR - this.GENERATION_LENGTH;
			
			ClearHelper.clearUser(this.db, request.getUsername());
			FillHelper.resetFields(this.db, this.fNamesArray, this.mNamesArray,
					this.sNamesArray, this.locationsArray);
			FillHelper.beginFill();

			Person newFilledUser = new Person();
			newFilledUser.setDescendant(prevFilledUser.getUserName());
			newFilledUser.setPersonID(prevFilledUser.getPersonId());
			System.out.println("here");
			Report report = FillHelper.createFamily(newFilledUser,
					birthDate, request.getLevels(), request.getUsername());
			System.out.println("here");
			if (!report.getStatus()) {
				FillHelper.endFill(report.getStatus());
				return new FillResponse("There was an " +
						"error loading the DB. Error message: " + report.getMessage(),report.getStatus());
				
			}

/*
			report = FillHelper.createEvents(newFilledUser, birthDate, request.getLevels(), request.getUsername());
			if (!report.getStatus()) {
				FillHelper.endFill(report.getStatus());
				return new FillResponse("There was an " +
						"error loading the DB. Error message: " + report.getMessage(),report.getStatus());
				
			}*/
			
			FillHelper.endFill(true);
			int personCount = FillHelper.getPersonCount();
			int eventCount = FillHelper.getEventCount();
			
			this.responseString = "Successfully added " + 
					personCount + " persons and " +
					eventCount + " events to the database.";
			this.processSuccessful = true;
			return new FillResponse(this.responseString, this.processSuccessful);
			
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
