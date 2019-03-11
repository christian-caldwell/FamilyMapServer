package Data.DAO;

import Data.*;
import java.sql.Connection;
import Model.*;
import Data.Helpers.*;
import Data.DAO.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DBAccessPersons 
{
	private DataBase db;

	public DBAccessPersons(DataBase db)
	{
		this.db = db;

	}
	
	public void createTable(Connection connect) throws SQLException {

		String sql = "CREATE TABLE IF NOT EXISTS PERSONS"+
				"("+
				"personid varchar(64) primary key,"+
				"descendant varchar(64),"+
				"father varchar(64),"+
				"mother varchar(64),"+
				"spouse varchar(64),"+
				"firstName varchar(64),"+
				"lastName varchar(64),"+
				"gender varchar(64)"+
				");";
		
		PreparedStatement statement = connect.prepareStatement(sql);
		statement.executeUpdate();
	}
	
	public void resetTable(Connection connect) throws SQLException {
		String sql = "DROP TABLE IF EXISTS persons; ";
		PreparedStatement statement = connect.prepareStatement(sql);
		statement.executeUpdate();
		createTable(connect);
	}
	
	
	public boolean addPersonToTable(Person newPerson) throws SQLException
	{
		if(newPerson.getPersonID() == null)
		{
			newPerson.setPersonID(PersonHelper.generatePersonID());
		}
		boolean success = false;
		String sql = "insert into persons (personid, descendant, " +
				"father, mother, spouse, firstName, lastName, " +
				"gender) values (?, ?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement statment = null;
	    ResultSet keyRS = null;

		try
		{

			statment = db.connection.prepareStatement(sql);
			statment.setString(1, newPerson.getPersonID());
			statment.setString(2, newPerson.getDescendant());
			statment.setString(3, newPerson.getFather());
			statment.setString(4, newPerson.getMother());
			statment.setString(5, newPerson.getSpouse());
			statment.setString(6, newPerson.getFirstName());
			statment.setString(7, newPerson.getLastName());
			statment.setString(8, newPerson.getGender());

			
			if(statment.executeUpdate() != 1)
			{
				throw new SQLException();
			}
			else {
				success = true;
			}
		}
		catch(SQLException e)
		{
			throw e;
		}
		finally
		{
			if(statment != null) {
				statment.close();
			}
		}
		return success;
	}

	public List<Person> findUserPersonSet(String givenUsername) throws SQLException
	{
		String sql = "select * from persons where persons.descendant = ?";
		List<Person> allPerson = new ArrayList<Person>();

		PreparedStatement statement = null;
		ResultSet result = null;
		try
		{
			statement = db.connection.prepareStatement(sql);
			statement.setString(1, givenUsername);
			result = statement.executeQuery();

			
			while(result.next())
			{
				Person foundPerson = new Person();
				foundPerson.setPersonID(result.getString(1));
				foundPerson.setDescendant(result.getString(2));
				foundPerson.setFather(result.getString(3));
				foundPerson.setMother(result.getString(4));
				foundPerson.setSpouse(result.getString(5));
				foundPerson.setFirstName(result.getString(6));
				foundPerson.setLastName(result.getString(7));
				foundPerson.setGender(result.getString(8));

				allPerson.add(foundPerson);
			}					
		}
		catch(SQLException e)
		{
			throw e;
		}
		finally
		{
			if(statement != null) {
				statement.close();
			}

			if (result != null) {
				result.close();
			}
		}

		if (allPerson.isEmpty()) {
			return null;
		}
		else {
			return allPerson;
		}
	}


	public Person findPersonByPerson(String givenPersonID) throws SQLException
	{
		if (givenPersonID == null) {
			return null;
		}

		PreparedStatement statement = null;
		String sql = "select * from persons where persons.personid = ?";

		ResultSet result = null;
		Person foundPerson = null;

		try
		{
			statement = db.connection.prepareStatement(sql);
			statement.setString(1, givenPersonID);
			result = statement.executeQuery();
			int i = 0;
			if(result.next())
			{
				i++;
				foundPerson = new Person();
				foundPerson.setPersonID(result.getString(1));
				foundPerson.setDescendant(result.getString(2));
				foundPerson.setFather(result.getString(3));
				foundPerson.setMother(result.getString(4));
				foundPerson.setSpouse(result.getString(5));
				foundPerson.setFirstName(result.getString(6));
				foundPerson.setLastName(result.getString(7));
				foundPerson.setGender(result.getString(8));
			}
			System.out.println(i);
		}
		catch(SQLException e)
		{
			throw e;
		}
		finally
		{
			if(statement != null) {
				statement.close();
			}
			if (result != null) {
				result.close();
			}
		}
		return foundPerson;
	}

}
