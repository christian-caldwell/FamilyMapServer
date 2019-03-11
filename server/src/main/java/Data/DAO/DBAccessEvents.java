package Data.DAO;

import Data.DataBase;
import java.sql.Connection;

import Model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DBAccessEvents 
{
	private DataBase db;
	
	public DBAccessEvents(DataBase db)
	{
		this.db = db;
	}
	
	public void createTable(Connection connect) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS EVENTS"+
				"("+
				"eventid varchar(64) primary key,"+
				"descendant varchar(64),"+
				"personid varchar(64),"+
				"longitude real,"+
				"latitude real,"+
				"year real," +
				"city varchar(25),"+
				"country varchar(25),"+
				"description varchar(50)"+

				");";
		PreparedStatement statement = connect.prepareStatement(sql);
		statement.executeUpdate();
		
	}
	
	public void resetTable(Connection connect) throws SQLException {
		String sql = "DROP TABLE IF EXISTS events; ";
		PreparedStatement statement = connect.prepareStatement(sql);
		statement.executeUpdate();
		createTable(connect);
	}
	
	public void addEventToTable(Event newEvent) throws SQLException
	{
		PreparedStatement statement = null;
		String sql = "insert into events (eventid, descendant, " +
				"personid, longitude, latitude,  year, description," +
				" country, city) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try
		{
			statement = db.connection.prepareStatement(sql);
			statement.setString(1, newEvent.getEventID());
			statement.setString(2, newEvent.getDescendant());
			statement.setString(3, newEvent.getPersonID());
			statement.setDouble(4, newEvent.getLongitude());
			statement.setDouble(5, newEvent.getLatitude());
			statement.setInt(6, newEvent.getYear());
			statement.setString(7, newEvent.getCity());
			statement.setString(8, newEvent.getCountry());
			statement.setString(9, newEvent.getDescription());
			
			if(statement.executeUpdate() != 1)
			{
				throw new SQLException();
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

		}

	}

	public List<Event> findUserEventSetByUser(String username) throws SQLException
	{
		List<Event> combinedEventList = new ArrayList<Event>();
		List<Person> persons = db.personTable.findUserPersonSet(username);

		for(int i = 0; i < persons.size(); i++)
		{
			System.out.println(persons.get(i).getPersonID()+ persons.size() + " " +combinedEventList.isEmpty());
			combinedEventList.addAll(db.eventsTable.findEventSetByPerson(persons.get(i).getPersonID()));
		}
		System.out.println("free");
		return combinedEventList;
		
	}
	public Event findEventByEvent(String givenEventID) throws SQLException
	{
		PreparedStatement statement = null;
		ResultSet results = null;

		Event foundEvent = null;
		String sql = "select * from events where events.eventid = ?";

		try
		{
			statement = db.connection.prepareStatement(sql);
			statement.setString(1, givenEventID);
			results = statement.executeQuery();

			if(results.next())
			{
				foundEvent = new Event();
				foundEvent.setEventID(results.getString(1));
				foundEvent.setDescendant(results.getString(2));
				foundEvent.setPersonID(results.getString(3));
				foundEvent.setLongitude(results.getDouble(4));
				foundEvent.setLatitude(results.getDouble(5));
				foundEvent.setYear(results.getInt(6));
				foundEvent.setCity(results.getString(7));
				foundEvent.setCountry(results.getString(8));
				foundEvent.setDescription(results.getString(9));

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

			if (results != null) {
				results.close();
			}
		}
		return foundEvent;
	}

	public List<Event> findEventSetByPerson(String givenPersonID) throws SQLException
	{

		String sql = "select * from events where events.personid = ?";

		PreparedStatement statement = null;
		ResultSet results = null;
		List<Event> allEvents = new ArrayList<Event>();;

		try
		{
			statement = db.connection.prepareStatement(sql);
			statement.setString(1, givenPersonID);
			results = statement.executeQuery();

			while(results.next())
			{
				Event personEvent = new Event();
				personEvent.setEventID(results.getString(1));
				personEvent.setDescendant(results.getString(2));
				personEvent.setPersonID(results.getString(3));
				personEvent.setLatitude(results.getDouble(4));
				personEvent.setLongitude(results.getDouble(5));
				personEvent.setYear(results.getInt(6));
				personEvent.setCity(results.getString(7));
				personEvent.setCountry(results.getString(8));
				personEvent.setDescription(results.getString(9));

				allEvents.add(personEvent);
			}
		}
		catch(SQLException e)
		{
			System.out.println("this part isnt working, while loop reordered, please fix");
			throw e;
		}
		finally
		{
			if (results != null) {
				results.close();
			}

			if(statement != null) {
				statement.close();
			}
		}


		return allEvents;

	}
}
