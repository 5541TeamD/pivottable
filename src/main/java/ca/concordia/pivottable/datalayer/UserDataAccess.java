package ca.concordia.pivottable.datalayer;

/**
 * Defines the interface for user database access operations.
 * @author 	Jyotsana Gupta
 * @version	1.0
 */
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
	 * Deletes an existing user record and its corresponding schema and sharing records from the user database.
	 * @param 	username		Username of the user to be deleted
	 * @return	true, if the user is successfully deleted
	 * 			false, otherwise
	 */
	boolean deleteUser(String username);
}
