package Data.Helpers;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import Model.Event;
import Model.Person;
import Data.DataBase;
import Model.Report;

public class FillHelper {
	private static int personsAdded;
	private static int eventsAdded;
	private static DataBase db;
	private static JsonArray fNamesArray;
	private static JsonArray mNamesArray;
	private static JsonArray sNamesArray;
	private static JsonArray locationsArray;
	
	final private int GENERATION_LENGTH = 30;
	final private int CURRENT_YEAR = 2018;
	final private int LIFE_SPAN =80;
	final private int MARRAGE_YEAR = 25;
	final private int FIRST_JOB = 15;
	final private int EMPLOYMENT_PERIOD = 25;
	final private int RETIREMENT_YEAR = 60;
	
	private int totalLevels;
	private int levelsLeft;
	private Person person;
	private int birthYearStart; 
	private Report report;
	
	public static void resetFields(DataBase newdb, JsonArray newfNamesArray, 
			JsonArray newmNamesArray, JsonArray newsNamesArray, JsonArray newlocationsArray) {
		personsAdded = 0;
		eventsAdded = 0;
		db = newdb;
		fNamesArray = newfNamesArray;
		mNamesArray = newmNamesArray;
		sNamesArray = newsNamesArray;
		locationsArray = newlocationsArray;
	}
	
	public static int getPersonCount() {
		return personsAdded;
	}
	
	public static int getEventCount() {
		return eventsAdded;
	}
	
	public static void beginFill() {
		db.startTransaction();
	}
	
	public static void endFill(boolean success) {
		db.closeTransaction(success);
	}

	public static Report createFamily(Person person, int birthYearStart, int totalLevels, String username) {
		FillHelper treeMaker = new FillHelper(person, birthYearStart, totalLevels);
		treeMaker.fillTree(person, totalLevels ,birthYearStart, username);
		return treeMaker.report;
	}
	
	public static Report createEvents(Person person, int birthYearStart, int totalLevels, String username) {
		FillHelper eventMaker = new FillHelper(person, birthYearStart, totalLevels);
		eventMaker.fillEvents(person, birthYearStart, username);
		return eventMaker.report;
	}
	
	public FillHelper(Person person,int birthYearStart, int totalLevels) {
		this.totalLevels = totalLevels;
		this.levelsLeft = totalLevels;
		this.person = person;
		this.birthYearStart = birthYearStart;
		this.report = new Report("DataBase Access Successful", true);
	}

	public Person fillTree(Person child, int levelsToGo, int childBorn, String username)
	{
		
		if(levelsToGo <= 0) //base case for the recursion
		{
			if(child == this.person) {
				if (this.person.getFirstName() == null) {
					child = generateRootPerson(child, childBorn, username);

				}
				fillEvents(child, childBorn, username);
				PersonHelper.addPerson(child, db);
				personsAdded++;
				return child;
			}
			else {
				PersonHelper.addPerson(child, db);
				personsAdded++;
				return null;
			}
		}
		
		levelsToGo--;
		
		int birthYear = childBorn - this.GENERATION_LENGTH;
		Person father = fillPerson(true, birthYear, username);
		Person mother = fillPerson(false, birthYear, username);

		if(father != null && mother != null)
		{
			marry(father, mother, username);
			child.setFather(father.getPersonID());
			child.setMother(mother.getPersonID());
			father = fillTree(father, levelsToGo, birthYear, username);
			mother = fillTree(mother, levelsToGo, birthYear, username);
		}

		if(child == this.person && this.person.getFirstName() == null) {
			child = generateRootPerson(child, childBorn, username);
		}

		PersonHelper.addPerson(child, db);
		personsAdded++;
		return child;
	}

	public Person generateRootPerson(Person originalUser, int birthYear, String username)
	{
		Random coinFlip = new Random();
		if(coinFlip.nextBoolean())
		{
			originalUser.setFirstName((mNamesArray.get((int)(coinFlip.nextDouble() * mNamesArray.size()))).getAsString());
			originalUser.setGender("m");
		}
		else
		{
			originalUser.setFirstName((fNamesArray.get((int)(coinFlip.nextDouble() * fNamesArray.size()))).getAsString());
			originalUser.setGender("f");
		}

		originalUser.setLastName((sNamesArray.get((int)(coinFlip.nextDouble() * sNamesArray.size()))).getAsString());
		fillEvents(originalUser, birthYear, username);
		///this.person.setSpouse("N/A");
		return this.person;
	}

	public Person fillPerson(boolean male, int birthYear,String username)
	{
		Person person = new Person();
		person.setDescendant(username);
	    person.setPersonID(PersonHelper.generatePersonID());
		Random rand = new Random();
	    
	    if(male)
	    {
	    	person.setFirstName((mNamesArray.get((int)(rand.nextDouble() * mNamesArray.size()))).getAsString());
	    	person.setGender("m");
	    }
	    else
	    {
	    	person.setFirstName((fNamesArray.get((int)(rand.nextDouble() * fNamesArray.size()))).getAsString());
	    	person.setGender("f");
	    }

	    person.setLastName((sNamesArray.get((int)(rand.nextDouble() * sNamesArray.size()))).getAsString());
	    fillEvents(person, birthYear, username);

    	return person;
	
	}
	
	public void fillEvents(Person person, int birthYearStart, String name)
	{
		Random rand = new Random();
		int birthYear = birthYearStart;
		int deathYear = birthYear + this.LIFE_SPAN;
		
		int firstJob = this.FIRST_JOB + birthYear;
		int retired = this.RETIREMENT_YEAR + birthYear;
		
		makeEvent(person, "birth", birthYear, name);
		if(deathYear < this.CURRENT_YEAR) {
			makeEvent(person, "death", deathYear, name);
		}
		
		int firstInt = rand.nextInt(); 
		int secondInt =rand.nextInt(); 
		makeEvent(person, "first job", firstJob, name);
		makeEvent(person, "second job", firstJob + 5, name);
		if (firstInt > secondInt) {
			makeEvent(person, "third job", firstJob + this.EMPLOYMENT_PERIOD * (secondInt / firstInt), name);
			makeEvent(person, "retirement", retired + 10 * (secondInt / firstInt), name);
		}
		else {
			makeEvent(person, "retirement", retired  + 10 * (firstInt / secondInt), name);
		}
	}
	
	private void marry(Person father, Person mother, String name)
	{
		try {
			List<Event> events;
		
				events = db.eventsTable.findEventSetByPerson(father.getPersonID());
			
			Random rand = new Random();
			if(events != null && events.size() > 0)
			{
				//Collections.sort(events);
				int marriageYear = events.get(0).getYear() + this.MARRAGE_YEAR + 10 * ((rand.nextInt() + 10) % 10);
				
				Event marriage = makeEvent(father, "marriage", marriageYear, name);
				
				marriage.setPersonID(mother.getPersonID());
				marriage.setEventID("E-" + Math.abs(rand.nextInt()) + "-" + Math.abs(rand.nextInt()));
				db.eventsTable.addEventToTable(marriage);
				eventsAdded++;
				
				father.setSpouse(mother.getPersonID());
				mother.setSpouse(father.getPersonID());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			this.report = new Report(e.getMessage(),false);
		}
	}
	private Event makeEvent(Person person, String describe, int year, String username)
	{
		Event event = new Event();
		try {
			Random rand = new Random();

			event.setDescendant(username);
			event.setEventID("E-" + Math.abs(rand.nextInt()) + "-" + Math.abs(rand.nextInt()));
		    event.setPersonID(person.getPersonID());
		    
		    JsonObject location;
		    do
		    {
			    location = locationsArray.get(
			    		(int)(rand.nextDouble() * (locationsArray.size()-1))).getAsJsonObject();
		    }while(!location.has("longitude") || !location.has("latitude") || !location.has("country")
		    		|| !location.has("city"));
		    
		    event.setLongitude(location.get("longitude").getAsDouble());
		    event.setLatitude(location.get("latitude").getAsDouble());
		    event.setCountry(location.get("country").getAsString());
		    event.setCity(location.get("city").getAsString());
		    event.setDescription(describe);
		    event.setYear(year);
		    db.eventsTable.addEventToTable(event);
		    eventsAdded++;
		} catch (SQLException e) {
			e.printStackTrace();
			this.report = new Report(e.getMessage(),false);
		}
	    
	    return event;
	}

}
