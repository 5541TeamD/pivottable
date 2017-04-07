package ca.concordia.pivottable.servicelayer.impl;

import java.util.List;
import ca.concordia.pivottable.datalayer.SchemaDataAccess;
import ca.concordia.pivottable.datalayer.UserDataAccess;
import ca.concordia.pivottable.datalayer.impl.SchemaDataAccessImpl;
import ca.concordia.pivottable.datalayer.impl.UserDataAccessImpl;
import ca.concordia.pivottable.entities.ShareableSchema;
import ca.concordia.pivottable.servicelayer.SchemaManagementService;
import ca.concordia.pivottable.utils.PivotTableException;

/**
 * Handles shareable schema management operations, including schema creation, editing, sharing and retrieving.
 * @author	Jyotsana Gupta
 * @version	1.0
 */
public class SchemaManagementServiceImpl implements SchemaManagementService
{	
	/**
	 * User schema database object used for performing data processing operations.
	 */
	private SchemaDataAccess schemaDatabase = new SchemaDataAccessImpl();
	
	/**
	 * User database object used for performing data processing operations.
	 */
	private UserDataAccess userDatabase = new UserDataAccessImpl();
	
	/**
	 * Adds a new shareable schema to the user schema database.
	 * @param 	shareableSchema	Details of the shareable schema
	 */
	public void createShareableSchema(ShareableSchema shareableSchema)
	{
		//Parsing shareable schema object
		String schemaName = shareableSchema.getSchemaName();
		String ownerUsername = shareableSchema.getOwnerUsername();
		String dbURL = shareableSchema.getDbURL();
		String dbUsername = shareableSchema.getDbUsername();
		String dbPassword = shareableSchema.getDbPassword();
		String pvtTblSchema = shareableSchema.getPvtTblSchema().toJSON();
		
		//Validating the new schema details
		String schemaValidation = validateSchema(schemaName, ownerUsername, dbURL, dbUsername, pvtTblSchema);
		if (schemaValidation != null)
			throw new PivotTableException("Unable to validate new shareable schema before creation. " + schemaValidation);
		else
		{
			//Adding the new shareable schema to the user schema database
			boolean schemaAdded = schemaDatabase.addShareableSchema(schemaName, ownerUsername, dbURL, dbUsername, dbPassword, pvtTblSchema);
			
			//Throwing an exception to the UI in case schema creation fails
			if (!schemaAdded)
				throw new PivotTableException("Unable to create new shareable schema " + schemaName + ".");
		}
	}
	
	/**
	 * Validates the details of a shareable schema before creation/update.
	 * @param 	schemaName		Name of the schema
	 * @param 	ownerUsername	Username of the schema owner
	 * @param 	dbURL			URL of the database in which the schema is defined
	 * @param 	dbUsername		Username for logging into the database in which the schema is defined
	 * @param 	pvtTblSchema	Selections made as part of schema definition
	 * @return	Schema validation error message, if validation fails
	 * 			null, otherwise
	 */
	private String validateSchema(String schemaName, String ownerUsername, String dbURL, String dbUsername, String pvtTblSchema)
	{
		String schemaValidation = null;
		
		if ((schemaName == null) || (schemaName.trim().isEmpty()))
			schemaValidation = "Schema name is either blank or contains only whitespaces.";
		else if ((ownerUsername == null) || (ownerUsername.trim().isEmpty()))
			schemaValidation = "Owner username is either blank or contains only whitespaces.";
		else if ((dbURL == null) || (dbURL.trim().isEmpty()))
			schemaValidation = "DB URL is either blank or contains only whitespaces.";
		else if ((dbUsername == null) || (dbUsername.trim().isEmpty()))
			schemaValidation = "DB username is either blank or contains only whitespaces.";
		else if ((pvtTblSchema == null) || (pvtTblSchema.trim().isEmpty()))
			schemaValidation = "Pivot table schema selection is blank.";
		
		return schemaValidation;
	}
	
	/**
	 * Updates the details of an existing shareable schema in the user schema database.
	 * @param	updatedSchema	Updated details of the shareable schema
	 */
	public void updateShareableSchema(ShareableSchema updatedSchema)
	{
		//Parsing shareable schema object
		long schemaID = updatedSchema.getSchemaID();
		String schemaName = updatedSchema.getSchemaName();
		String ownerUsername = updatedSchema.getOwnerUsername();
		String dbURL = updatedSchema.getDbURL();
		String dbUsername = updatedSchema.getDbUsername();
		String dbPassword = updatedSchema.getDbPassword();
		String pvtTblSchema = updatedSchema.getPvtTblSchema().toJSON();
		
		//Validating the updated schema details
		String schemaValidation = validateSchema(schemaName, ownerUsername, dbURL, dbUsername, pvtTblSchema);
		if (schemaValidation != null)
			throw new PivotTableException("Unable to validate updated shareable schema. " + schemaValidation);
		else
		{
			//Updating the shareable schema details in the user schema database
			boolean schemaUpdated = schemaDatabase.updateShareableSchema(schemaID, schemaName, ownerUsername, dbURL, dbUsername, dbPassword, pvtTblSchema);
			
			//Throwing an exception to the UI in case schema update fails
			if (!schemaUpdated)
				throw new PivotTableException("Unable to update shareable schema " + schemaID + ".");
		}
	}
	
	/**
	 * Deletes an existing shareable schema and its sharing records from the user schema database.
	 * @param	schemaID	ID of the shareable schema to be deleted
	 */
	public void deleteShareableSchema(long schemaID)
	{
		boolean schemaDeleted = false;
		
		//Deleting the shareable schema from the user schema database
		boolean schemaExists = schemaDatabase.schemaExists(schemaID);
		if (!schemaExists)
			schemaDeleted = false;
		else
			schemaDeleted = schemaDatabase.deleteShareableSchema(schemaID);
		
		//Throwing an exception to the UI in case schema deletion fails
		if (!schemaDeleted)
			throw new PivotTableException("Unable to delete shareable schema " + schemaID + ".");
	}
	
	/**
	 * Adds a new sharing for a schema between two users in the user schema database.
	 * @param 	sharedUsername		Username of the shared user
	 * @param 	shareableSchemaID	ID of the shared schema
	 */
	public void shareSchema(String sharedUsername, long shareableSchemaID)
	{
		String schemaShareStatus = null;
		
		//Checking if the shared username exists in the user database
		userDatabase.connect();
		boolean usernameExists = userDatabase.usernameExists(sharedUsername.toLowerCase());
		if (!usernameExists)
			schemaShareStatus = "User " + sharedUsername + " does not exist. Sharing request denied!";
		else
		{
			//Checking if the owner itself is the shared user
			String ownerUsername = schemaDatabase.getOwnerUsername(shareableSchemaID);
			if (ownerUsername.equalsIgnoreCase(sharedUsername))
				schemaShareStatus = "Owner user " + sharedUsername + " is trying to share schema " + shareableSchemaID + " with itself. Sharing request denied!";
		}
		
		//If shared username is valid
		if (schemaShareStatus == null)
		{
			//Creating a new sharing for this schema in the user database
			boolean schemaShared = schemaDatabase.addSchemaSharing(sharedUsername, shareableSchemaID);
			
			if (!schemaShared)
				schemaShareStatus = "Unable to share schema " + shareableSchemaID + " with user " + sharedUsername + " .";
		}
		
		//Throwing an exception to the UI in case schema sharing fails
		if (schemaShareStatus != null)
			throw new PivotTableException(schemaShareStatus);
	}
	
	/**
	 * Deletes an existing sharing of a schema between two users in the user schema database.
	 * @param 	sharedUsername		Username of the shared user
	 * @param 	sharedSchemaID		ID of the shared schema
	 */
	public void unshareSchema(String sharedUsername, long sharedSchemaID)
	{
		//Deleting the schema sharing record from the user schema database
		boolean sharingDeleted = schemaDatabase.deleteSchemaSharing(sharedUsername, sharedSchemaID);
		
		//Throwing an exception to the UI in case schema sharing deletion fails
		if (!sharingDeleted)
			throw new PivotTableException("Unable to unshare schema " + sharedSchemaID + " with shared user " + sharedUsername + ".");
	}
	
	/**
	 * Fetches a list of all shareable schemas owned by a user.
	 * @param 	ownerUsername	Username of the owner user
	 * @return	List of all owned shareable schema details: schema ID and schema name
	 */
	public List<String[]> getMyOwnedSchemas(String ownerUsername)
	{
		List<String[]> myOwnedSchemaList = schemaDatabase.getMyOwnedSchemas(ownerUsername);
		
		return myOwnedSchemaList;
	}
	
	/**
	 * Fetches a list of all shareable schemas owned by a user that are shared with other users as well.
	 * @param 	ownerUsername	Username of the owner user
	 * @return	List of all shared shareable schema details: schema ID, schema name and shared username
	 */
	public List<String[]> getMySharedSchemas(String ownerUsername)
	{
		List<String[]> mySharedSchemaList = schemaDatabase.getMySharedSchemas(ownerUsername);
		
		return mySharedSchemaList;
	}
	
	/**
	 * Fetches a list of all schemas shared with a user but owned by other users.
	 * @param 	sharedUsername	Username of the shared user
	 * @return	List of all shared schema details: schema ID, schema name and owner username
	 */
	public List<String[]> getSchemasSharedWithMe(String sharedUsername)
	{
		List<String[]> sharedWithMeSchemaList = schemaDatabase.getSchemasSharedWithMe(sharedUsername);
		
		return sharedWithMeSchemaList;
	}
}