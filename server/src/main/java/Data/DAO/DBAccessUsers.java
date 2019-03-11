package Data.DAO;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import Data.*;
import Model.*;
import Data.Helpers.*;
import Data.DAO.*;
import java.util.Random;
		
public class DBAccessUsers 
{
	private DataBase db;

	public DBAccessUsers(DataBase db)
	{
		this.db = db;
	}
	
	public void createTable(Connection connect) throws SQLException {
		String sql1 = "CREATE TABLE IF NOT EXISTS USERS"+
				"("+
				"username varchar(64) primary key,"+
				"personId varchar(64),"+
				"password varchar(64),"+
				"firstName varchar(64),"+
				"lastName varchar(64),"+
				"email varchar(64),"+
				"gender varchar(10)"+
				");";

		PreparedStatement statementUser = connect.prepareStatement(sql1);
		statementUser.executeUpdate();

		String sql2 = "CREATE TABLE IF NOT EXISTS TOKENS"+
				"("+
				"authorization varchar(64) primary key,"+
				"username varchar(64),"+
				"personId varchar(64)"+
				");";

		PreparedStatement statmentAuth = connect.prepareStatement(sql2);
		statmentAuth.executeUpdate();
	}
	
	public void resetTable(Connection connect) throws SQLException {
		String sql1 = "DROP TABLE users;";
		PreparedStatement statementUser = connect.prepareStatement(sql1);
		statementUser.executeUpdate();

		String sql2 = "DROP TABLE tokens;";
		PreparedStatement statmentAuth = connect.prepareStatement(sql2);
		statmentAuth.executeUpdate();
		createTable(connect);
	}
	
	public boolean addUserToTable(User registeringUser) throws SQLException
	{
		String sql = "insert into users (userName, personId, password," +
				" firstName, lastName, email, gender) values (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement statement = null;
		boolean success = false;
		try
		{
			statement = db.connection.prepareStatement(sql);
			statement.setString(1, registeringUser.getUserName());
			statement.setString(2, registeringUser.getPersonId());
			statement.setString(3, registeringUser.getPassword());
			statement.setString(4, registeringUser.getFirstName());
			statement.setString(5, registeringUser.getLastName());
			statement.setString(6, registeringUser.getEmail());
			statement.setString(7, registeringUser.getGender());
			if(statement.executeUpdate() != 1)
			{
				throw new SQLException();
			}
			else {
				success = true;
			}
		}
		catch(SQLException e)
		{
			System.out.print(e.getMessage());
			throw e;
		}
		finally
		{
			if(statement != null) {
				statement.close();
			}
		}
		
		return success;
	}
	
	private AuthToken addAuthTokenToTable(User authorizedUser) throws SQLException
	{
		PreparedStatement statement = null;
		String sql = "insert into tokens (authorization, userName," +
				" personId) values (?, ?, ?)";
		AuthToken createdAuthTest = null;
		try
		{
			String newToken = generateNewToken();
			statement = db.connection.prepareStatement(sql);
			statement.setString(1, newToken);
			statement.setString(2, authorizedUser.getUserName());
			statement.setString(3, authorizedUser.getPersonId());
			if(statement.executeUpdate() == 1)
			{
				createdAuthTest = new AuthToken();
				createdAuthTest.setAuthorization(newToken);
				createdAuthTest.setUserName(authorizedUser.getUserName());
				createdAuthTest.setPersonID(authorizedUser.getPersonId());
			}
			else
			{
				throw new SQLException();
			}
		}
		catch(SQLException e)
		{
			System.out.print(e.getMessage());
			throw e;
		}
		finally
		{
			if(statement != null) {
				statement.close();
			}
		}
		
		return createdAuthTest;
	}
	
	public AuthToken confirmPassword(User loggingUser) throws SQLException
	{
		User confirmedUser = findUserByUserName(loggingUser.getUserName());
		if(confirmedUser == null) {
			return null;
		}
		else if(confirmedUser.getPassword().equals(loggingUser.getPassword()))
		{
			AuthToken generatedToken = addAuthTokenToTable(confirmedUser);
			return generatedToken;
		}
		else {
			return null;
		}
	}
	
	public User checkAuthToken(String inputAuthToken) throws SQLException
	{
		try 
		{
			return findUserByAccessToken(inputAuthToken);
		} 
		catch (SQLException e) 
		{
			throw e;
		}
	}
	
	public User findUserByUserName(String givenUsername) throws SQLException
	{
		if (givenUsername == null) {
			return null;
		}
		User foundUser = null;
		String sql = "select * from users where users.username = ?";

		PreparedStatement statement = null;
		ResultSet result = null;

		try
		{
			statement = db.connection.prepareStatement(sql);
			statement.setString(1, givenUsername);
			result = statement.executeQuery();
			
			if(result.next())
			{
				foundUser = new User();
				foundUser.setUserName(result.getString(1));
				foundUser.setPersonId(result.getString(2));
				foundUser.setPassword(result.getString(3));
				foundUser.setFirstName(result.getString(4));
				foundUser.setLastName(result.getString(5));
				foundUser.setEmail(result.getString(6));
				foundUser.setGender(result.getString(7));

			}
		}
		catch(SQLException e)
		{
			System.out.print(e.getMessage());
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
		return foundUser;
	}
	
	public User findUserByAccessToken(String givenToken) throws SQLException
	{
		AuthToken foundToken = new AuthToken();
		String sql = "select * from tokens where tokens.authorization = ?";

		PreparedStatement statement = null;
		ResultSet result = null;
		try
		{
			statement = db.connection.prepareStatement(sql);
			statement.setString(1, givenToken);
			result = statement.executeQuery();;
			while(result.next())
			{
				foundToken.setAuthorization(result.getString(1));
				foundToken.setUserName(result.getString(2));
				foundToken.setPersonID(result.getString(3));

			}
		}
		catch(SQLException e)
		{
			System.out.print(e.getMessage());
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
		return findUserByUserName(foundToken.getUserName());
	}
	
	private String generateNewToken()
	{
		Random randomNum = new Random();
		return "T-" + Math.abs(randomNum.nextInt()) + "-" + Math.abs(randomNum.nextInt());
	}
	
}
