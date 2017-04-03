package ca.concordia.pivottable.datalayer;

public interface UserDataAccess 
{
	/**
	 * Initiates a connection with the user database.
	 */
	void connect();
	
	/**
	 * Closes the connection with the user database.
	 * @return	true, if connection is closed successfully, or if the connection was already closed
	 * <br>		false, if the attempt to disconnect fails
	 */
    boolean disconnect();
	
    /**
     * Checks if a username already exists in the user database.
     * @param	username	Username to be verified
     * @return	true, if username exists
     * 			false, otherwise
     */
	boolean usernameExists(String username);
	
	/**
	 * Adds a record for a new user into the application users table in the user database.
	 * @param 	username		Username of the user
	 * @param 	passwordHash	Hashed password of the user
	 * @return	true, if the user is successfully added
	 * 			false, otherwise
	 */
	boolean addUser(String username, String passwordHash);
	
	/**
	 * Fetches password hash for a user from the user database.
	 * @param 	username	Username for the user
	 * @return	Password hash for the user
	 * 			null, if password hash cannot be fetched
	 */
	String getUserPasswordHash(String username);
	
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
	boolean addShareableSchema(String schemaName, String ownerUsername, String dbURL, String dbUsername, String dbPassword, String pvtTblSchema);
}
