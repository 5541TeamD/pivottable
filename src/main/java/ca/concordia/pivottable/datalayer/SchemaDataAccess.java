package ca.concordia.pivottable.datalayer;

import java.util.List;

/**
 * Defines the interface for user shareable schema data access operations.
 * @author 	Jyotsana Gupta
 * @version	1.0
 */
public interface SchemaDataAccess 
{
	/**
	 * Initiates a connection with the user schema database.
	 */
	void connect();
	
	/**
	 * Closes the connection with the user schema database.
	 * @return	true, if connection is closed successfully, or if the connection was already closed
	 * <br>		false, if the attempt to disconnect fails
	 */
    boolean disconnect();
    
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
	boolean addShareableSchema(String schemaName, String ownerUsername, String dbURL, String dbUsername, String dbPassword, String pvtTblSchema);
	
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
	boolean updateShareableSchema(long schemaID, String schemaName, String ownerUsername, String dbURL, String dbUsername, String dbPassword, String pvtTblSchema);
	
	/**
	 * Deletes an existing shareable schema and its sharing records from the user schema database.
	 * @param	schemaID	ID of the shareable schema to be deleted
	 * @return	true, if the schema is successfully deleted
	 * 			false, otherwise
	 */
	boolean deleteShareableSchema(long schemaID);
	
	/**
	 * Fetches owner username for a shareable schema from the user schema database.
	 * @param 	shareableSchemaID	ID of the shareable schema
	 * @return	Owner username for the shareable schema
	 * 			null, if the owner username cannot be fetched
	 */
	String getOwnerUsername(long shareableSchemaID);
	
	/**
	 * Adds a record for a new schema sharing into the shared schemas table in the user schema database.
	 * @param 	sharedUsername		Username of the shared user
	 * @param 	shareableSchemaID	ID of the shared schema
	 * @return	true, if the schema sharing is successfully added
	 * 			false, otherwise
	 */
	boolean addSchemaSharing(String sharedUsername, long shareableSchemaID);
	
	/**
	 * Deletes an existing sharing record of a schema between two users from the the shared schemas table in the user schema database.
	 * @param 	sharedUsername		Username of the shared user
	 * @param 	sharedSchemaID		ID of the shared schema
	 * @return	true, if the schema sharing is successfully deleted
	 * 			false, otherwise
	 */
	boolean deleteSchemaSharing(String sharedUsername, long sharedSchemaID);
	
	/**
	 * Fetches a list of all shareable schemas owned by a user from the user schema database.
	 * @param	ownerUsername	Username of the owner user
	 * @return	List of all owned shareable schema details: schema ID and schema name
	 */
	List<String[]> getMyOwnedSchemas(String ownerUsername);
	
	/**
	 * Fetches a list of all shareable schemas owned by a user that are shared with other users from the user schema database.
	 * @param 	ownerUsername	Username of the owner user
	 * @return	List of all shared shareable schema details: schema ID, schema name and shared username
	 */
	List<String[]> getMySharedSchemas(String ownerUsername);
	
	/**
	 * Fetches a list of all schemas shared with a user but owned by other users from the user schema database.
	 * @param 	sharedUsername	Username of the shared user
	 * @return	List of all shared schema details: schema ID, schema name and owner username
	 */
	List<String[]> getSchemasSharedWithMe(String sharedUsername);
}