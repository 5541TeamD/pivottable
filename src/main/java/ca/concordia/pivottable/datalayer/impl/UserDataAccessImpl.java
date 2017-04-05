package ca.concordia.pivottable.datalayer.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import ca.concordia.pivottable.datalayer.UserDataAccess;
//TODO
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Handles all the operations to be performed on the user database.
 * @author 	Jyotsana Gupta
 * @version	1.0
 */
public class UserDataAccessImpl implements UserDataAccess
{
	/**
	 * URL of the database to be connected to.
	 */
	private String dbUrl;
	
	/**
	 * Username to login to the database.
	 */
	private String dbUsername;
	
	/**
	 * Password to login to the database.
	 */
	private String dbPassword;
	
	/**
	 * Database connection object
	 */
	Connection dbConnection = null;
	
	/**
	 * Used for logging information, warning and error messages during application run.
	 */
	//TODO
	//private Logger log = LoggerFactory.getLogger(UserDataAccessImpl.class);
	
	/**
	 * Class constructor.
	 */
	public UserDataAccessImpl()
	{
		dbUrl = "jdbc:mysql://localhost:3306/app_user_db?useSSL=false";
		dbUsername = "root";
		dbPassword = "root";
	}
	
	/**
	 * Initiates a connection with the user database.
	 */
	public void connect()
	{
		//Database driver required for connection
		String jdbcDriver = "com.mysql.jdbc.Driver";
		
		try
		{
			//Loading JDBC Driver class at run-time
			Class.forName(jdbcDriver);
		}
		catch (Exception excp)
		{
			//TODO
			//log.error("Unexpected exception occurred while attempting user DB connection... " + excp.getMessage());
		}		
		
		//TODO
		//log.info("Initiating connection to user database " + dbUrl + "...");
		
		try
		{
			dbConnection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		}
		catch (SQLException sqle)
		{
			dbConnection = null;
			//TODO
			//log.error("SQLException occurred while connecting to user database... " + sqle.getMessage());
		}
	}
	
	/**
	 * Closes the connection with the user database.
	 * @return	true, if connection is closed successfully, or if the connection was already closed
	 * <br>		false, if the attempt to disconnect fails
	 */
    public boolean disconnect()
    {
    	if (dbConnection != null)
    	{
    		try
    		{
    			dbConnection.close();
    		}
    		catch (SQLException sqle)
    		{
    			//TODO
    			//log.error("SQLException occurred while disconnecting from user database... " + sqle.getMessage());
    			return false;
    		}
    	}
    	
    	return true;
    }
	
    /**
     * Checks if a username already exists in the user database.
     * @param	username	Username to be verified
     * @return	true, if username exists
     * 			false, otherwise
     */
	public boolean usernameExists(String username)
	{
		int usernameCount = -1;
		
		if (dbConnection == null)							//failed connection
  		{
  			return false;
  		}
  		
  		//Proceeding, if database connection is successful
  		ResultSet rsUsernameCount = null;  		
  		try
  		{
  			//Fetching count of the username to be validated from the user database
  			String usernameQuery = "SELECT COUNT(*) FROM application_user WHERE username = ? ;";
  			PreparedStatement prepStmt = dbConnection.prepareStatement(usernameQuery);
  			prepStmt.setString(1, username);
  			
  			rsUsernameCount = prepStmt.executeQuery(usernameQuery);
  			prepStmt.close();
  			
  			usernameCount = rsUsernameCount.getInt(1);
  			rsUsernameCount.close();
  		}
  		catch (SQLException sqle)
  		{
  			rsUsernameCount = null;
  			//TODO
  			//log.error("SQLException occurred while fetching username count from user database... " + sqle.getMessage());
  		}
  		finally
  		{
  			disconnect();
  		}
  		  		
  		if (usernameCount > 0)
  			return true;
  		else
  			return false;
	}
	
	/**
	 * Adds a record for a new user into the application users table in the user database.
	 * @param 	username		Username of the user
	 * @param 	passwordHash	Hashed password of the user
	 * @return	true, if the user is successfully added
	 * 			false, otherwise
	 */
	public boolean addUser(String username, String passwordHash)
	{
		boolean userAdded = true;
		
		if (dbConnection == null)							//failed connection
  		{
  			return false;
  		}
		
		//Proceeding, if database connection is successful
		try
		{
			//Inserting a record for the new user into the application users table
			String insertUserStmt = "INSERT INTO application_user VALUES( ? , ? );";
			
			dbConnection.setAutoCommit(false);
			PreparedStatement prepStmt = dbConnection.prepareStatement(insertUserStmt);
			prepStmt.setString(1, username);
			prepStmt.setString(2, passwordHash);
			
	  		prepStmt.executeUpdate(insertUserStmt);
	  		dbConnection.commit();
	  		prepStmt.close();
		}
		catch(SQLException sqle)
		{
			userAdded = false;
			String errMsg = sqle.getMessage();
			try
			{
				dbConnection.rollback();
			}
			catch(SQLException rollbackExcp)
			{
				errMsg = errMsg + " and " + rollbackExcp.getMessage();
			}
			
			//TODO
  			//log.error("SQLException occurred while adding new user to database... " + errMsg);
  		}
		finally
		{
			disconnect();
		}
  		
		return userAdded;
	}
	
	/**
	 * Fetches password hash for a user from the user database.
	 * @param 	username	Username for the user
	 * @return	Password hash for the user
	 * 			null, if password hash cannot be fetched
	 */
	public String getUserPasswordHash(String username)
	{
		String passwordHash = null;
		
		if (dbConnection == null)							//failed connection
  		{
  			return null;
  		}
  		
  		//Proceeding, if database connection is successful
  		ResultSet rsUserPassword = null;  		
  		try
  		{
  			//Fetching password hash of user from the user database
  			String passwordQuery = "SELECT passwordHash FROM application_user WHERE username = ? ;";
  			PreparedStatement prepStmt = dbConnection.prepareStatement(passwordQuery);
  			prepStmt.setString(1, username);
  			
  			rsUserPassword = prepStmt.executeQuery(passwordQuery);
  			prepStmt.close();
  			
  			passwordHash = rsUserPassword.getString(1);
  			rsUserPassword.close();
  		}
  		catch (SQLException sqle)
  		{
  			rsUserPassword = null;
  			//TODO
  			//log.error("SQLException occurred while fetching password hash from user database... " + sqle.getMessage());
  		}
  		finally
  		{
  			disconnect();
  		}
  		  		
  		return passwordHash;
	}
	
	/**
	 * Deletes an existing user record and its corresponding schema and sharing records from the user database.
	 * @param 	username		Username of the user to be deleted
	 * @return	true, if the user is successfully deleted
	 * 			false, otherwise
	 */
	public boolean deleteUser(String username)
	{
		boolean userDeleted = true;
		
		if (dbConnection == null)							//failed connection
  		{
  			return false;
  		}
		
		//Proceeding, if database connection is successful
		try
		{	
			//Deleting the user from the application users table
			String deleteUserStmt = "DELETE FROM application_user "
										+ " WHERE username = ? ;";
			dbConnection.setAutoCommit(false);
			PreparedStatement prepStmt = dbConnection.prepareStatement(deleteUserStmt);
			prepStmt.setString(1, username);
			
			prepStmt.executeUpdate(deleteUserStmt);
  			dbConnection.commit();
  			prepStmt.close();
		}
		catch(SQLException sqle)
		{
			userDeleted = false;
			String errMsg = sqle.getMessage();
			try
			{
				dbConnection.rollback();
			}
			catch(SQLException rollbackExcp)
			{
				errMsg = errMsg + " and " + rollbackExcp.getMessage();
			}
			
			//TODO
  			//log.error("SQLException occurred while deleting user from database... " + errMsg);
  		}
		finally
		{
			disconnect();
		}
  		
		return userDeleted;
	}
}
