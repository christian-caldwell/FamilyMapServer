package Data.Helpers;

import java.sql.SQLException;
import java.util.Random;

import Data.DataBase;
import Model.Person;
import Model.Report;

public class PersonHelper {

	public static Report addPerson(Person person, DataBase db) {
		//db.startTransaction();
		try {
			db.personTable.addPersonToTable(person);
			//db.closeTransaction(true);
			return new Report("Successful Add", true);
		} catch (SQLException e) {
			e.printStackTrace();
			return new Report(e.getMessage(),false);
		}
	}

	public static Person getPersonByID(DataBase db, String personID, String username)
	{
		Person person = null;
		db.startTransaction();
		try
		{
			person = db.personTable.findPersonByPerson(personID);
			if(person == null || !person.getDescendant().equals(username))
				person = null;
			db.closeTransaction(true);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			db.closeTransaction(false);
		}
		
		return person;
	}

	public static String generatePersonID() {
		Random rand = new Random();
		return "P-" + Math.abs(rand.nextInt()) + "-" + Math.abs(rand.nextInt());
	}
}
