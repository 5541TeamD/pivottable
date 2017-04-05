package ca.concordia.pivottable.datalayer.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ca.concordia.pivottable.datalayer.SchemaDataAccess;
//TODO
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Handles all the operations to be performed on the user schema database.
 * @author 	Jyotsana Gupta
 * @version	1.0
 */
public class SchemaDataAccessImpl implements SchemaDataAccess 
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
	//private Logger log = LoggerFactory.getLogger(SchemaDataAccessImpl.class);
	
	/**
	 * Class constructor.
	 */
	public SchemaDataAccessImpl()
	{
		dbUrl = "jdbc:mysql://localhost:3306/app_user_db?useSSL=false";
		dbUsername = "root";
		dbPassword = "root";
	}
	
	/**
	 * Initiates a connection with the user schema database.
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
			//log.error("Unexpected exception occurred while attempting user schema DB connection... " + excp.getMessage());
		}		
		
		//TODO
		//log.info("Initiating connection to user schema database " + dbUrl + "...");
		
		try
		{
			dbConnection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		}
		catch (SQLException sqle)
		{
			dbConnection = null;
			//TODO
			//log.error("SQLException occurred while connecting to user schema database... " + sqle.getMessage());
		}
	}
	
	/**
	 * Closes the connection with the user schema database.
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
    			//log.error("SQLException occurred while disconnecting from user schema database... " + sqle.getMessage());
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    /**
	 * Adds a record for a new shareable schema into the shareable schemas table in the user schema database.
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
										+ "VALUES(NULL, ? , ? , ? , ? , ? , ? );";
			
			dbConnection.setAutoCommit(false);
			PreparedStatement prepStmt = dbConnection.prepareStatement(insertSchemaStmt);
  			prepStmt.setString(1, schemaName);
  			prepStmt.setString(2, ownerUsername);
  			prepStmt.setString(3, dbURL);
  			prepStmt.setString(4, dbUsername);
  			prepStmt.setString(5, dbPassword);
  			prepStmt.setString(6, pvtTblSchema);
			
  			prepStmt.executeUpdate(insertSchemaStmt);
  			dbConnection.commit();
  			prepStmt.close();
		}
		catch(SQLException sqle)
		{
			schemaAdded = false;
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
			//log.error("SQLException occurred while adding new shareable schema to database... " + errMsg);
  		}
		finally
		{
			disconnect();
		}
  		
		return schemaAdded;
	}
	
	/**
	 * Updates an existing shareable schema record in the shareable schemas table in the user schema database.
	 * @param 	schemaID		ID of the shareable schema
	 * @param 	schemaName		Updated name of the shareable schema
	 * @param 	ownerUsername	Username of the owner user
	 * @param 	dbURL			Updated URL of the database in which the schema exists
	 * @param 	dbUsername		Updated username to login to the database used while creating/updating the schema
	 * @param 	dbPassword		Updated password to login to the database used while creating/updating the schema
	 * @param 	pvtTblSchema	Updated details of the selections made in the schema
	 * @return	true, if the schema is successfully updated
	 * 			false, otherwise
	 */
	public boolean updateShareableSchema(long schemaID, String schemaName, String ownerUsername, String dbURL, String dbUsername, String dbPassword, String pvtTblSchema)
	{
		boolean schemaUpdated = true;
		
		if (dbConnection == null)							//failed connection
  		{
  			return false;
  		}
		
		//Proceeding, if database connection is successful
		try
		{
			//Updating the schema record in the shareable schema table
			String updateSchemaStmt = "UPDATE shareable_schema "
										+ " SET schema_name = ? "
										+ " , db_url = ? "
										+ " , db_username = ? "
										+ " , db_password = ? "
										+ " , pivot_table_schema = ? "
										+ " WHERE schema_id = ? ;";
			
			dbConnection.setAutoCommit(false);
			PreparedStatement prepStmt = dbConnection.prepareStatement(updateSchemaStmt);
			prepStmt.setString(1, schemaName);
			prepStmt.setString(2, dbURL);
			prepStmt.setString(3, dbUsername);
			prepStmt.setString(4, dbPassword);
			prepStmt.setString(5, pvtTblSchema);
			prepStmt.setLong(6, schemaID);
			
  			prepStmt.executeUpdate(updateSchemaStmt);
  			dbConnection.commit();
  			prepStmt.close();
		}
		catch(SQLException sqle)
		{
			schemaUpdated = false;
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
  			//log.error("SQLException occurred while updating shareable schema in database... " + errMsg);
  		}
		finally
		{
			disconnect();
		}
  		
		return schemaUpdated;
	}
	
	/**
	 * Deletes an existing shareable schema and its sharing records from the user schema database.
	 * @param	schemaID	ID of the shareable schema to be deleted
	 * @return	true, if the schema is successfully deleted
	 * 			false, otherwise
	 */
	public boolean deleteShareableSchema(long schemaID)
	{
		boolean schemaDeleted = true;
		
		if (dbConnection == null)							//failed connection
  		{
  			return false;
  		}
		
		//Proceeding, if database connection is successful
		try
		{	
			//Deleting the shareable schema from the shareable schemas table
			String deleteSchemaStmt = "DELETE FROM shareable_schema "
										+ " WHERE schema_id = ? ;";
			dbConnection.setAutoCommit(false);
			PreparedStatement prepStmt = dbConnection.prepareStatement(deleteSchemaStmt);
			prepStmt.setLong(1, schemaID);
			
			prepStmt.executeUpdate(deleteSchemaStmt);
  			dbConnection.commit();
  			prepStmt.close();
		}
		catch(SQLException sqle)
		{
			schemaDeleted = false;
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
  			//log.error("SQLException occurred while deleting shareable schema from database... " + errMsg);
  		}
		finally
		{
			disconnect();
		}
  		
		return schemaDeleted;
	}
	
	/**
	 * Fetches owner username for a shareable schema from the user schema database.
	 * @param 	shareableSchemaID	ID of the shareable schema
	 * @return	Owner username for the shareable schema
	 * 			null, if the owner username cannot be fetched
	 */
	public String getOwnerUsername(long shareableSchemaID)
	{
		String ownerUsername = null;
		
		if (dbConnection == null)							//failed connection
  		{
  			return null;
  		}
  		
  		//Proceeding, if database connection is successful
  		ResultSet rsOwnerUser = null;  		
  		try
  		{
  			//Fetching owner username of shareable schema from the user schema database
  			String ownerQuery = "SELECT owner_username FROM shareable_schema WHERE schema_id = ? ;";
  			PreparedStatement prepStmt = dbConnection.prepareStatement(ownerQuery);
  			prepStmt.setLong(1, shareableSchemaID);
  			
  			rsOwnerUser = prepStmt.executeQuery(ownerQuery);
  			prepStmt.close();
  			
  			ownerUsername = rsOwnerUser.getString(1);
  			rsOwnerUser.close();
  		}
  		catch (SQLException sqle)
  		{
  			rsOwnerUser = null;
  			//TODO
  			//log.error("SQLException occurred while fetching owner username from user schema database... " + sqle.getMessage());
  		}
  		finally
  		{
  			disconnect();
  		}
  		  		
  		return ownerUsername;
	}
	
	/**
	 * Adds a record for a new schema sharing into the shared schemas table in the user schema database.
	 * @param 	sharedUsername		Username of the shared user
	 * @param 	shareableSchemaID	ID of the shared schema
	 * @return	true, if the schema sharing is successfully added
	 * 			false, otherwise
	 */
	public boolean addSchemaSharing(String sharedUsername, long shareableSchemaID)
	{
		boolean sharingAdded = true;
		
		if (dbConnection == null)							//failed connection
  		{
  			return false;
  		}
		
		//Proceeding, if database connection is successful
		try
		{
			//Inserting a record for the new schema sharing into the shared schemas table
			String insertSharingStmt = "INSERT INTO shared_schema VALUES( ? , ? );";
			
			dbConnection.setAutoCommit(false);
			PreparedStatement prepStmt = dbConnection.prepareStatement(insertSharingStmt);
			prepStmt.setString(1, sharedUsername);
			prepStmt.setLong(2, shareableSchemaID);
			
	  		prepStmt.executeUpdate(insertSharingStmt);
	  		dbConnection.commit();
	  		prepStmt.close();
		}
		catch(SQLException sqle)
		{
			sharingAdded = false;
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
  			//log.error("SQLException occurred while adding new schema sharing to user schema database... " + errMsg);
  		}
		finally
		{
			disconnect();
		}
  		
		return sharingAdded;
	}
	
	/**
	 * Deletes an existing sharing record of a schema between two users from the the shared schemas table in the user schema database.
	 * @param 	sharedUsername		Username of the shared user
	 * @param 	sharedSchemaID		ID of the shared schema
	 * @return	true, if the schema sharing is successfully deleted
	 * 			false, otherwise
	 */
	public boolean deleteSchemaSharing(String sharedUsername, long sharedSchemaID)
	{
		boolean sharingDeleted = true;
		
		if (dbConnection == null)							//failed connection
  		{
  			return false;
  		}
		
		//Proceeding, if database connection is successful
		try
		{	
			//Deleting the schema sharing from the shared schemas table
			String deleteSharingStmt = "DELETE FROM shared_schema "
										+ " WHERE shared_username = ? "
										+ " AND shared_schema_id = ? ;";
			dbConnection.setAutoCommit(false);
			PreparedStatement prepStmt = dbConnection.prepareStatement(deleteSharingStmt);
			prepStmt.setString(1, sharedUsername);
			prepStmt.setLong(2, sharedSchemaID);
			
			prepStmt.executeUpdate(deleteSharingStmt);
  			dbConnection.commit();
  			prepStmt.close();
		}
		catch(SQLException sqle)
		{
			sharingDeleted = false;
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
  			//log.error("SQLException occurred while deleting schema sharing from database... " + errMsg);
  		}
		finally
		{
			disconnect();
		}
  		
		return sharingDeleted;
	}
	
	/**
	 * Fetches a list of all shareable schemas owned by a user from the user schema database.
	 * @param	ownerUsername	Username of the owner user
	 * @return	List of all owned shareable schema details: schema ID and schema name
	 */
	public List<String[]> getMyOwnedSchemas(String ownerUsername)
	{
		List<String[]> myOwnedSchemaList = new ArrayList<String[]>();
		
		if (dbConnection == null)							//failed connection
  		{
  			return null;
  		}
  		
  		//Proceeding, if database connection is successful
  		ResultSet rsMyOwnedSchemas = null;  		
  		try
  		{
  			//Fetching all the shareable schemas owned by the user from the user schema database
  			String ownedSchemaQuery = "SELECT schema_id, schema_name FROM shareable_schema WHERE owner_username = ? ;";
  			PreparedStatement prepStmt = dbConnection.prepareStatement(ownedSchemaQuery);
  			prepStmt.setString(1, ownerUsername);
  			
  			rsMyOwnedSchemas = prepStmt.executeQuery(ownedSchemaQuery);
  			prepStmt.close();
  			
  			//Preparing a list of all fetched schema details
  			while (rsMyOwnedSchemas.next())
  			{
  				String[] schemaDetails = new String[2];
  				
  				schemaDetails[0] = rsMyOwnedSchemas.getObject("schema_id").toString();
  				schemaDetails[1] = rsMyOwnedSchemas.getObject("schema_name").toString();
  				
  				myOwnedSchemaList.add(schemaDetails);
  			}
  			
  			rsMyOwnedSchemas.close();
  		}
  		catch (SQLException sqle)
  		{
  			rsMyOwnedSchemas = null;
  			//TODO
  			//log.error("SQLException occurred while fetching all shareable schemas owned by user from user schema database... " + sqle.getMessage());
  		}
  		finally
  		{
  			disconnect();
  		}
  		  		
  		return myOwnedSchemaList;
	}
	
	/**
	 * Fetches a list of all shareable schemas owned by a user that are shared with other users from the user schema database.
	 * @param 	ownerUsername	Username of the owner user
	 * @return	List of all shared shareable schema details: schema ID, schema name and shared username
	 */
	public List<String[]> getMySharedSchemas(String ownerUsername)
	{
		List<String[]> mySharedSchemaList = new ArrayList<String[]>();
		
		if (dbConnection == null)							//failed connection
  		{
  			return null;
  		}
  		
  		//Proceeding, if database connection is successful
  		ResultSet rsMySharedSchemas = null;  		
  		try
  		{
  			//Fetching all the shareable schemas owned by the user that are shared with other users from the user schema database
  			String sharedSchemaQuery = "SELECT shareable.schema_id, shareable.schema_name, shared.shared_username "
  										+ " FROM shareable_schema shareable, shared_schema shared "
  										+ " WHERE shareable.owner_username = ? "
  										+ " AND shareable.schema_id = shared.shared_schema_id;";
  			PreparedStatement prepStmt = dbConnection.prepareStatement(sharedSchemaQuery);
  			prepStmt.setString(1, ownerUsername);
  			
  			rsMySharedSchemas = prepStmt.executeQuery(sharedSchemaQuery);
  			prepStmt.close();
  			
  			//Preparing a list of all fetched schema details
  			while (rsMySharedSchemas.next())
  			{
  				String[] schemaDetails = new String[3];
  				
  				schemaDetails[0] = rsMySharedSchemas.getObject("schema_id").toString();
  				schemaDetails[1] = rsMySharedSchemas.getObject("schema_name").toString();
  				schemaDetails[2] = rsMySharedSchemas.getObject("shared_username").toString();
  				
  				mySharedSchemaList.add(schemaDetails);
  			}
  			
  			rsMySharedSchemas.close();
  		}
  		catch (SQLException sqle)
  		{
  			rsMySharedSchemas = null;
  			//TODO
  			//log.error("SQLException occurred while fetching all shareable schemas owned by a user and shared with others from user schema database... " + sqle.getMessage());
  		}
  		finally
  		{
  			disconnect();
  		}
  		  		
  		return mySharedSchemaList;
	}
	
	/**
	 * Fetches a list of all schemas shared with a user but owned by other users from the user schema database.
	 * @param 	sharedUsername	Username of the shared user
	 * @return	List of all shared schema details: schema ID, schema name and owner username
	 */
	public List<String[]> getSchemasSharedWithMe(String sharedUsername)
	{
		List<String[]> sharedWithMeSchemaList = new ArrayList<String[]>();
		
		if (dbConnection == null)							//failed connection
  		{
  			return null;
  		}
  		
  		//Proceeding, if database connection is successful
  		ResultSet rssharedWithMeSchemas = null;  		
  		try
  		{
  			//Fetching all the schemas shared with a user but owned by other users from the user schema database
  			String sharedSchemaQuery = "SELECT shareable.schema_id, shareable.schema_name, shareable.owner_username "
  										+ " FROM shareable_schema shareable, shared_schema shared "
  										+ " WHERE shared.shared_username = ? "
  										+ " AND shareable.schema_id = shared.shared_schema_id;";
  			PreparedStatement prepStmt = dbConnection.prepareStatement(sharedSchemaQuery);
  			prepStmt.setString(1, sharedUsername);
  			
  			rssharedWithMeSchemas = prepStmt.executeQuery(sharedSchemaQuery);
  			prepStmt.close();
  			
  			//Preparing a list of all fetched schema details
  			while (rssharedWithMeSchemas.next())
  			{
  				String[] schemaDetails = new String[3];
  				
  				schemaDetails[0] = rssharedWithMeSchemas.getObject("schema_id").toString();
  				schemaDetails[1] = rssharedWithMeSchemas.getObject("schema_name").toString();
  				schemaDetails[2] = rssharedWithMeSchemas.getObject("owner_username").toString();
  				
  				sharedWithMeSchemaList.add(schemaDetails);
  			}
  			
  			rssharedWithMeSchemas.close();
  		}
  		catch (SQLException sqle)
  		{
  			rssharedWithMeSchemas = null;
  			//TODO
  			//log.error("SQLException occurred while fetching all schemas shared with a user but owned by other users from user schema database... " + sqle.getMessage());
  		}
  		finally
  		{
  			disconnect();
  		}
  		  		
  		return sharedWithMeSchemaList;
	}
}
