package Data.Helpers;

import java.sql.SQLException;

import Data.DataBase;
import Model.User;
import Model.Person;
import Model.Event;

public class LoadHelper {
	public static boolean loadUser(DataBase db, User user)
	{
		boolean success = false;
		db.startTransaction();
		try
		{
			db.usersTable.addUserToTable(user);
			//now that user is in the user table and has a personId
			db.closeTransaction(true);
			
			//transactions are automatically handled by the DataImporter
			success = true;

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			db.closeTransaction(false);
		}
		return success;
	}
	
	public static boolean loadPerson(DataBase db, Person person)
	{
		boolean success = false;
		db.startTransaction();
		try
		{
			db.personTable.addPersonToTable(person);;
			//now that user is in the user table and has a personId
			db.closeTransaction(true);
			
			//transactions are automatically handled by the DataImporter
			success = true;

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			db.closeTransaction(false);
		}
		return success;
	}
	
	public static boolean loadEvent(DataBase db, Event event)
	{
		boolean success = false;
		db.startTransaction();
		try
		{
			db.eventsTable.addEventToTable(event);
			//now that user is in the user table and has a personId
			db.closeTransaction(true);
			
			//transactions are automatically handled by the DataImporter
			success = true;

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			db.closeTransaction(false);
		}
		return success;
	}
	
	

}
