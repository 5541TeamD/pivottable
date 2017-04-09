package ca.concordia.pivottable.servicelayer;

import java.util.List;

import ca.concordia.pivottable.entities.ShareableSchema;

/**
 * Defines the interface for shareable schema management service operations.
 * @author 	Jyotsana Gupta
 * @version	1.0
 */
public interface SchemaManagementService 
{
	/**
	 * Adds a new shareable schema to the user schema database.
	 * @param 	shareableSchema	Details of the shareable schema
	 * @return The generated ID
	 */
	Long createShareableSchema(ShareableSchema shareableSchema);
	
	/**
	 * Updates the details of an existing shareable schema in the user schema database.
	 * @param	updatedSchema	Updated details of the shareable schema
	 */
	void updateShareableSchema(ShareableSchema updatedSchema);
	
	/**
	 * Deletes an existing shareable schema and its sharing records from the user schema database.
	 * @param	schemaID	ID of the shareable schema to be deleted
	 */
	void deleteShareableSchema(long schemaID);
	
	/**
	 * Adds a new sharing for a schema between two users in the user schema database.
	 * @param 	sharedUsername		Username of the shared user
	 * @param 	shareableSchemaID	ID of the shared schema
	 */
	void shareSchema(String sharedUsername, long shareableSchemaID);
	
	/**
	 * Deletes an existing sharing of a schema between two users in the user schema database.
	 * @param 	sharedUsername		Username of the shared user
	 * @param 	sharedSchemaID		ID of the shared schema
	 */
	void unshareSchema(String sharedUsername, long sharedSchemaID);
	
	/**
	 * Fetches a list of all shareable schemas owned by a user.
	 * @param 	ownerUsername	Username of the owner user
	 * @return	List of all owned shareable schema details: schema ID and schema name
	 */
	List<String[]> getMyOwnedSchemas(String ownerUsername);
	
	/**
	 * Fetches a list of all shareable schemas owned by a user that are shared with other users as well.
	 * @param 	ownerUsername	Username of the owner user
	 * @return	List of all shared shareable schema details: schema ID, schema name and shared username
	 */
	List<String[]> getMySharedSchemas(String ownerUsername);
	
	/**
	 * Fetches a list of all schemas shared with a user but owned by other users.
	 * @param 	sharedUsername	Username of the shared user
	 * @return	List of all shared schema details: schema ID, schema name and owner username
	 */
	List<String[]> getSchemasSharedWithMe(String sharedUsername);

	/**
	 * Gets the schema by Id
	 * @param id The schema id
	 * @param requestor The username who requested this schema
	 * @return The shareable schema
	 */
	ShareableSchema getSchemaById(Long id, String requestor);
}
