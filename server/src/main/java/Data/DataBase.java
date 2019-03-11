package Data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Data.DAO.*;

public class DataBase 
{
	
	public DBAccessPersons personTable;
	public DBAccessEvents eventsTable;
	public DBAccessUsers usersTable;
	
	public Connection connection;
	
	public DataBase()
	{
		loadDriver();
		this.personTable = new DBAccessPersons(this);
		this.eventsTable = new DBAccessEvents(this);
		this.usersTable = new DBAccessUsers(this);
	}
	/**
	 * Load the driver to talk to the database
	 */
	public void loadDriver()
	{
		try
		{
			final String driver = "org.sqlite.JDBC";
			Class.forName(driver);
		}
		catch (ClassNotFoundException e)
		{
			System.out.print("Class Not found error\n");
		}
	}
	/**
	 * Open a connection with the data base
	 */
	public void openConnection()
	{
		//String dbName = "database.sqlite";
		File directory = new File("db");
		if(!directory.exists())
		{
			try
			{
				directory.mkdirs();
			}
			catch(SecurityException se)
			{
				System.out.println("Error creating the folder for the DB files! The server can not work correctly with out this!");
				return;
			}
		}
		
		String dbName = "db" + File.separator + "database.sqlite";
		String connectionURL = "jdbc:sqlite:" + dbName;
		connection = null;
		
		try
		{
			connection = DriverManager.getConnection(connectionURL);
			createIfNotExsit();
		}
		catch(SQLException e)
		{
			System.out.print("SQL error\n");
		}
		return;
		
		
	}

	public void startTransaction()
	{
		openConnection();
		try 
		{
			connection.setAutoCommit(false);
		} 
		catch (SQLException e)
		{
			System.out.print("turn off auto commit error");
			e.printStackTrace();
		}
	}
	
	public void closeTransaction(boolean commit) 
	{
		try
		{
			if(commit)
			{
				connection.commit();
			}
			else
			{
				connection.rollback();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.print("Close transaction commit error\n");
		}
		finally
		{
			try 
			{
				connection.close();
			} 
			catch (SQLException e) {
				System.out.println("Cant close connection");
				e.printStackTrace();
			}
		}
		connection = null;
	}
	
	public void createIfNotExsit() throws SQLException
	{
		this.eventsTable.createTable(this.connection);
		this.personTable.createTable(this.connection);
		this.usersTable.createTable(this.connection);
	}
	
	public void resetDB() throws SQLException
	{
		this.usersTable.resetTable(this.connection);
		this.eventsTable.resetTable(this.connection);
		this.personTable.resetTable(this.connection);
	}
	public void fillReset(String username) 
	{
		try
		{
			String personTable = "Delete from persons where descendant = ?";
			String eventTable = "Delete from events where descendant = ?";
			PreparedStatement stmt = this.connection.prepareStatement(personTable);
			PreparedStatement stmt2 = this.connection.prepareStatement(eventTable);
			
			stmt.setString(1, username);
			stmt2.setString(1, username);
			
			stmt.execute();
			stmt2.execute();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			
		}		
	}
}
