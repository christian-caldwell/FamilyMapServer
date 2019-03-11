package Data.Helpers;

import java.sql.SQLException;

import Data.DataBase;
import Model.User;
import Model.Report;

public class ClearHelper {
	public static Report clearDataBase(DataBase db)
	{
		boolean success = false;
		String message = "Clear Failed";
		db.startTransaction();
		try
		{
			db.resetDB();
			message = "Clear Successful";
			success = true;
			db.closeTransaction(true);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			db.closeTransaction(false);
		}
		
		return new Report (message,success);
	}
	
	public static Report clearUser(DataBase db, String username)
	{
		boolean success = false;
		String message = "User Clear Failed";
		db.startTransaction();

		db.fillReset(username);
		message = "User Clear Successful";
		success = true;
		db.closeTransaction(true);

		
		return new Report (message,success);
	}
}
