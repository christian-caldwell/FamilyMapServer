package Service.Load;

import java.sql.SQLException;
import java.util.ArrayList;

import Data.Helpers.ClearHelper;
import Model.Person;
import Model.User;
import Model.Event;

import Data.DataBase;
import Data.Helpers.LoadHelper;
import Model.Report;
import Service.Template.Provider;

public class LoadProvider  extends Provider {

	public LoadResponse execute(LoadRequest request) {
		ClearHelper.clearDataBase(db);

		ArrayList<User> loadedUsers = request.getUserList();
		for (User currUser: loadedUsers) {
			if (!LoadHelper.loadUser(db, currUser)) {
				return new LoadResponse("Failed to Load Users, Check Helper Func. Loop", false);
			}
		}

		ArrayList<Person> loadedPersons = request.getPersonList();
		for (Person currPerson: loadedPersons) {
			if (!LoadHelper.loadPerson(db, currPerson)) {
				return new LoadResponse("Failed to Load Persons, Check Helper Func. Loop", false);
			}
		}

		ArrayList<Event> loadedEvents = request.getEventList();
		for (Event currEvent: loadedEvents) {
			if (!LoadHelper.loadEvent(db, currEvent)) {
				return new LoadResponse("Failed to Load Events, Check Helper Func. Loop", false);
			}
		}

		return new LoadResponse("Successfully added " + loadedUsers.size() + " users, " 
				+ loadedPersons.size() + " persons, and " + loadedEvents.size() 
				+ " events to the database.", true);
	}
}
