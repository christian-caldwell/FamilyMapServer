package Data.Helpers;

import java.sql.SQLException;

import Data.DataBase;
import Model.User;
import Model.AuthToken;

public class AuthHelper {
	private static AuthToken newToken;
	
	public static AuthToken getNewToken() {
		return newToken;
	}
	
	public static boolean confirmPassword(DataBase db, User user)
	{
		boolean success = false;
		db.startTransaction();
		try
		{
			success = true;
			newToken = db.usersTable.confirmPassword(user);
			db.closeTransaction(true);
		}
		catch (SQLException e)
		{
			newToken = null;
			e.printStackTrace();
			db.closeTransaction(false);
		}
		
		return success;
	}
	
	public static User authenticateToken(DataBase db, String token)
	{
		db.startTransaction();
		try
		{

			User foundUser = db.usersTable.checkAuthToken(token);
			db.closeTransaction(true);
			return foundUser;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			db.closeTransaction(false);
		}
		return null;
	}


}
