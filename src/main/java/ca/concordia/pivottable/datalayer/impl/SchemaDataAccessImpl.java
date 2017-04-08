package ca.concordia.pivottable.datalayer.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import ca.concordia.pivottable.datalayer.SchemaDataAccess;
import ca.concordia.pivottable.entities.ApplicationConfiguration;
import ca.concordia.pivottable.entities.PivotTableSchema;
import ca.concordia.pivottable.entities.ShareableSchema;
import ca.concordia.pivottable.servicelayer.ConfigurationHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private Logger log = LoggerFactory.getLogger(SchemaDataAccessImpl.class);
	
	/**
	 * Class constructor.
	 */
	public SchemaDataAccessImpl(ConfigurationHolder appConfigHolder)
	{
		ApplicationConfiguration conf = appConfigHolder.getConfiguration();
		dbUrl = conf.getAppDatabaseUrl();
		dbUsername = conf.getAppDatabaseUser();
		dbPassword = conf.getAppDatabasePassword();
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
			log.error("Unexpected exception occurred while attempting user schema DB connection... " + excp.getMessage());
		}		
		
		log.info("Initiating connection to user schema database " + dbUrl + "...");
		
		try
		{
			dbConnection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		}
		catch (SQLException sqle)
		{
			dbConnection = null;
			log.error("SQLException occurred while connecting to user schema database... " + sqle.getMessage());
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
    			log.error("SQLException occurred while disconnecting from user schema database... " + sqle.getMessage());
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
	 * @return	the schema id if the schema is successfully added
	 * 			null, otherwise
	 */
	public Long addShareableSchema(String schemaName, String ownerUsername, String dbURL, String dbUsername, String dbPassword, String pvtTblSchema)
	{
		Long schemaAdded = null;
		
		//Connecting to data base
  		connect();
		
		if (dbConnection == null)							//failed connection
  		{
  			return null;
  		}
		
		//Proceeding, if database connection is successful
		try
		{
			//Inserting a record for the new schema into the shareable schema table
			String insertSchemaStmt = "INSERT INTO shareable_schema(schema_name, owner_username, db_url, db_username, db_password, pivot_table_schema) "
										+ "VALUES(? , ? , ? , ? , ? , ? );";
			
			dbConnection.setAutoCommit(false);
			PreparedStatement prepStmt = dbConnection.prepareStatement(insertSchemaStmt, Statement.RETURN_GENERATED_KEYS);
  			prepStmt.setString(1, schemaName);
  			prepStmt.setString(2, ownerUsername);
  			prepStmt.setString(3, dbURL);
  			prepStmt.setString(4, dbUsername);
  			prepStmt.setString(5, dbPassword);
  			prepStmt.setString(6, pvtTblSchema);
			
  			Integer ret = prepStmt.executeUpdate();
  			schemaAdded = ret.longValue();
  			dbConnection.commit();
  			prepStmt.close();
		}
		catch(SQLException sqle)
		{
			schemaAdded = null;
			String errMsg = sqle.getMessage();
			try
			{
				dbConnection.rollback();
			}
			catch(SQLException rollbackExcp)
			{
				errMsg = errMsg + " and " + rollbackExcp.getMessage();
			}
			
			log.error("SQLException occurred while adding new shareable schema to database... " + errMsg);
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
		
		//Connecting to data base
  		connect();
  		
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
			
  			prepStmt.executeUpdate();
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
			
			log.error("SQLException occurred while updating shareable schema in database... " + errMsg);
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
		
		//Connecting to data base
  		connect();
  		
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
			
			prepStmt.executeUpdate();
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
			
			log.error("SQLException occurred while deleting shareable schema from database... " + errMsg);
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
		
		//Connecting to data base
  		connect();
  		
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
  			
  			rsOwnerUser = prepStmt.executeQuery();
  			if (rsOwnerUser.next())
  				ownerUsername = rsOwnerUser.getString(1);
  			
  			rsOwnerUser.close();
  			prepStmt.close();
  		}
  		catch (SQLException sqle)
  		{
  			rsOwnerUser = null;
  			log.error("SQLException occurred while fetching owner username from user schema database... " + sqle.getMessage());
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
		
		//Connecting to data base
  		connect();
  		
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
			
	  		prepStmt.executeUpdate();
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
			
			log.error("SQLException occurred while adding new schema sharing to user schema database... " + errMsg);
		}
		finally
		{
			disconnect();
		}
  		
		return sharingAdded;
	}
	
	/**
     * Checks if a schema already exists in the user schema database.
     * @param	schemaID	Schema ID to be verified
     * @return	true, if schema exists
     * 			false, otherwise
     */
	public boolean schemaExists(long schemaID)
	{
		int schemaCount = -1;
		
		//Connecting to data base
  		connect();
  		
		if (dbConnection == null)							//failed connection
  		{
  			return false;
  		}
  		
  		//Proceeding, if database connection is successful
  		ResultSet rsSchemaCount = null;  		
  		try
  		{
  			//Fetching count of the schema ID to be validated from the user schema database
  			String schemaQuery = "SELECT COUNT(*) FROM shareable_schema WHERE schema_id = ?";
  			PreparedStatement prepStmt = dbConnection.prepareStatement(schemaQuery);
  			prepStmt.setLong(1, schemaID);
  			rsSchemaCount = prepStmt.executeQuery();
  			if (rsSchemaCount.next())
  				schemaCount = rsSchemaCount.getInt(1);
  			rsSchemaCount.close();
  			prepStmt.close();
  		}
  		catch (SQLException sqle)
  		{
  			rsSchemaCount = null;
  			log.error("SQLException occurred while fetching schema ID count from user schema database... " + sqle.getMessage());
  		}
  		  		
  		if (schemaCount > 0)
  			return true;
  		else
  			return false;
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
		
		//Connecting to data base
  		connect();
  		
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
			
			prepStmt.executeUpdate();
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
			
			log.error("SQLException occurred while deleting schema sharing from database... " + errMsg);
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
		
		//Connecting to data base
  		connect();
  		
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
  			
  			rsMyOwnedSchemas = prepStmt.executeQuery();
  			
  			//Preparing a list of all fetched schema details
  			while (rsMyOwnedSchemas.next())
  			{
  				String[] schemaDetails = new String[2];
  				
  				schemaDetails[0] = rsMyOwnedSchemas.getObject("schema_id").toString();
  				schemaDetails[1] = rsMyOwnedSchemas.getObject("schema_name").toString();
  				
  				myOwnedSchemaList.add(schemaDetails);
  			}
  			
  			rsMyOwnedSchemas.close();
  			prepStmt.close();
  		}
  		catch (SQLException sqle)
  		{
  			rsMyOwnedSchemas = null;
  			log.error("SQLException occurred while fetching all shareable schemas owned by user from user schema database... " + sqle.getMessage());
  		}
  		finally
  		{
  			disconnect();
  		}
  		  		
  		//Setting schema list to null in case no owned schemas are found
  		if (myOwnedSchemaList.size() == 0)
  			myOwnedSchemaList = null;
  		
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
		
		//Connecting to data base
  		connect();
  		
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
  			
  			rsMySharedSchemas = prepStmt.executeQuery();
  			
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
  			prepStmt.close();
  		}
  		catch (SQLException sqle)
  		{
  			rsMySharedSchemas = null;
  			log.error("SQLException occurred while fetching all shareable schemas owned by a user and shared with others from user schema database... " + sqle.getMessage());
  		}
  		finally
  		{
  			disconnect();
  		}
  		  		
  		//Setting schema list to null in case no shared schemas are found
  		if (mySharedSchemaList.size() == 0)
  			mySharedSchemaList = null;
  		
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
		
		//Connecting to data base
  		connect();
  		
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
  			
  			rssharedWithMeSchemas = prepStmt.executeQuery();
  			
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
  			prepStmt.close();
  		}
  		catch (SQLException sqle)
  		{
  			rssharedWithMeSchemas = null;
  			log.error("SQLException occurred while fetching all schemas shared with a user but owned by other users from user schema database... " + sqle.getMessage());
  		}
  		finally
  		{
  			disconnect();
  		}
  		  		
  		//Setting schema list to null in case no shared schemas are found
  		if (sharedWithMeSchemaList.size() == 0)
  			sharedWithMeSchemaList = null;
  		
  		return sharedWithMeSchemaList;
	}

	/**
	 * Fetches the schema from the database.
	 * @param id The schema id
	 * @return The schema object or null if not found
	 */
	@Override
	public ShareableSchema getSchemaById(Long id) {

		//Connecting to data base
		connect();

		ShareableSchema schema = null;

		if (dbConnection == null)							//failed connection
		{
			return null;
		}

		//Proceeding, if database connection is successful
		ResultSet rssharedSchemas = null;
		try
		{
			//Fetching all the schemas shared with a user but owned by other users from the user schema database
			String schemaQuery =
					"SELECT schema_id, schema_name, owner_username, db_url, db_username, db_password, pivot_table_schema FROM shareable_schema WHERE schema_id = ? ";
			PreparedStatement prepStmt = dbConnection.prepareStatement(schemaQuery);
			prepStmt.setLong(1, id);

			rssharedSchemas = prepStmt.executeQuery();

			//Preparing a list of all fetched schema details
			if (rssharedSchemas.next())
			{
				String[] schemaDetails = new String[6];

				schemaDetails[0] = rssharedSchemas.getString("schema_name");
				schemaDetails[1] = rssharedSchemas.getString("owner_username");
				schemaDetails[2] = rssharedSchemas.getString("db_url");
				schemaDetails[3] = rssharedSchemas.getString("db_username");
				schemaDetails[4] = rssharedSchemas.getString("db_password");
				schemaDetails[5] = rssharedSchemas.getString("pivot_table_schema");

				PivotTableSchema pvtTableSchema = PivotTableSchema.fromJSON(schemaDetails[5]);
				schema = new ShareableSchema(id, schemaDetails[0], schemaDetails[1],
						pvtTableSchema, schemaDetails[2], schemaDetails[3], schemaDetails[4]);

			}

			rssharedSchemas.close();
			prepStmt.close();
		}
		catch (SQLException sqle)
		{
			log.error("SQLException occurred while fetching schema with id " + String.valueOf(id) + " - " + sqle.getMessage());
		}
		finally
		{
			disconnect();
		}

		return schema;
	}
}
