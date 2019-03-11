package Data.Helpers;

import java.sql.SQLException;
import java.util.List;

import Data.DataBase;
import Model.Event;
import Model.Person;
import Model.User;

public class EventHelper {
	public static Event getEventByID(DataBase db, String eventID, String username)
	{
		Event event = null;
		db.startTransaction();
		try
		{
			event = db.eventsTable.findEventByEvent(eventID);
			if(event == null || !event.getDescendant().equals(username))
				event = null;
			db.closeTransaction(true);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			db.closeTransaction(false);
		}
		
		return event;
	}
	
	public static List<Event> getEventsByPerson(DataBase db, String personID)
	{
		List<Event> events = null;
		db.startTransaction();
		try
		{
			events = db.eventsTable.findEventSetByPerson(personID);
			db.closeTransaction(true);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			db.closeTransaction(false);
		}
		
		return events;
	}


	public static List<Event> getAllEventsFromFamilyByUser(DataBase db, String username)
	{
		List<Event> events = null;
		db.startTransaction();
		try
		{
			events = db.eventsTable.findUserEventSetByUser(username);
			System.out.println(username);
			db.closeTransaction(true);
		}catch(SQLException e)
		{
			e.printStackTrace();
			db.closeTransaction(false);
		}
		
		return events;
	}
}