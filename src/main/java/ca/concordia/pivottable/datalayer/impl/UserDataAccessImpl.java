package ca.concordia.pivottable.datalayer.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import ca.concordia.pivottable.datalayer.UserDataAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private Logger log = LoggerFactory.getLogger(UserDataAccessImpl.class);
	
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
			log.error("Unexpected exception occurred while attempting user DB connection... " + excp.getMessage());
		}		
		
		log.info("Initiating connection to user database " + dbUrl + "...");
		
		try
		{
			dbConnection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		}
		catch (SQLException sqle)
		{
			dbConnection = null;
			log.error("SQLException occurred while connecting to user database... " + sqle.getMessage());
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
    			log.error("SQLException occurred while disconnecting from user database... " + sqle.getMessage());
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
  			String usernameQuery = "SELECT COUNT(*) FROM application_user WHERE username = \'" + username + "\';";
  			Statement stmt = dbConnection.createStatement();
  			rsUsernameCount = stmt.executeQuery(usernameQuery);
  			usernameCount = rsUsernameCount.getInt(1);
  		}
  		catch (SQLException sqle)
  		{
  			rsUsernameCount = null;
  			log.error("SQLException occurred while fetching username count from user database... " + sqle.getMessage());
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
			String insertUserStmt = "INSERT INTO application_user VALUES(\'" + username + "\', \'" + passwordHash + "\');";
	  		Statement stmt = dbConnection.createStatement();
	  		stmt.executeUpdate(insertUserStmt);
		}
		catch(SQLException sqle)
		{
			userAdded = false;
			log.error("SQLException occurred while adding new user to database... " + sqle.getMessage());
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
  			String passwordQuery = "SELECT passwordHash FROM application_user WHERE username = \'" + username + "\';";
  			Statement stmt = dbConnection.createStatement();
  			rsUserPassword = stmt.executeQuery(passwordQuery);
  			passwordHash = rsUserPassword.getString(1);
  		}
  		catch (SQLException sqle)
  		{
  			rsUserPassword = null;
  			log.error("SQLException occurred while fetching password hash from user database... " + sqle.getMessage());
  		}
  		finally
  		{
  			disconnect();
  		}
  		  		
  		return passwordHash;
	}
	
	/**
	 * Adds a record for a new shareable schema into the shareable schemas table in the user database.
	 * @param 	schemaName		Name of the shareable schema
	 * @param 	ownerUsername	Username of the owner user
	 * @param 	dbURL			URL of the database in which the schema is created
	 * @param 	dbUsername		Username to login to the database used while creating the schema
	 * @param 	dbPassword		Password to login to the database used while creating the schema
	 * @param 	pvtTblSchema	Details of the selections made in the schema
	 * @return	true, if the schema is successfully added
	 * 			false, otherwise
	 */
	public boolean addShareableSchema(String schemaName, String ownerUsername, String dbURL, String dbUsername, String dbPassword, String pvtTblSchema)
	{
		boolean schemaAdded = true;
		
		if (dbConnection == null)							//failed connection
  		{
  			return false;
  		}
		
		//Proceeding, if database connection is successful
		try
		{
			//Inserting a record for the new schema into the shareable schema table
			String insertSchemaStmt = "INSERT INTO shareable_schema "
										+ "VALUES(NULL, "
										+ "\'" + schemaName + "\', "
										+ "\'" + ownerUsername + "\', "
										+ "\'" + dbURL + "\', "
										+ "\'" + dbUsername + "\', "
										+ "\'" + dbPassword + "\', "
										+ "\'" + pvtTblSchema + "\');";
	  		Statement stmt = dbConnection.createStatement();
	  		stmt.executeUpdate(insertSchemaStmt);
		}
		catch(SQLException sqle)
		{
			schemaAdded = false;
			log.error("SQLException occurred while adding new shareable schema to database... " + sqle.getMessage());
  		}
		finally
		{
			disconnect();
		}
  		
		return schemaAdded;
	}
}
