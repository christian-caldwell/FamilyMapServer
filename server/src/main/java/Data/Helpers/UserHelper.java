package Data.Helpers;

import java.sql.SQLException;
import java.util.List;

import Data.DataBase;
import Model.Person;
import Model.User;
import Service.Fill.FillHandler;

public class UserHelper {

	private static Person userPerson;

	public static Person getUserPerson() {
		return userPerson;
	}

	public static boolean registerUser(DataBase db, User user)
	{
		boolean success = false;
		db.startTransaction();
		try
		{

			Person person = new Person();
			person.setDescendant(user.getUserName());
			person.setPersonID(PersonHelper.generatePersonID());
			person.setFirstName(user.getFirstName());
			person.setLastName(user.getLastName());
			person.setGender(user.getGender());
			///person.setSpouse("N/A");
			///person.setMother("N/A");
			///person.setFather("N/A");
			user.setPersonId(person.getPersonID());

			success = db.usersTable.addUserToTable(user);
			db.closeTransaction(true);
			success = true;
			userPerson = person;

		}
		catch(SQLException e)
		{
			e.printStackTrace();
			db.closeTransaction(false);
		}
		return success;
	}
	
	
	
	public static User getUserByUsername(DataBase db, String username) 
	{
		User user = null;
		db.startTransaction();
		try
		{
			user = db.usersTable.findUserByUserName(username);;
			db.closeTransaction(true);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			db.closeTransaction(false);
		}
		return user;
	}


	public static User getUserByAccessToken(DataBase db, String token) 
	{
		User user = null;
		try
		{
			db.startTransaction();
			if(db.usersTable.checkAuthToken(token) != null)
			{
				user = db.usersTable.findUserByAccessToken(token);
			}
			db.closeTransaction(false);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return user;
	}

	public static List<Person> getUserNamesFamily(DataBase db, String username)
	{
		List<Person> persons = null;
		db.startTransaction();
		try 
		{
			persons = db.personTable.findUserPersonSet(username);
			db.closeTransaction(true);
		} catch (SQLException e) 
		{
			db.closeTransaction(false);
			e.printStackTrace();
		}
		
		return persons;
	}
	

}
