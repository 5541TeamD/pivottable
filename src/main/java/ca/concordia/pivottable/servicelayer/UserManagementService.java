package ca.concordia.pivottable.servicelayer;

import ca.concordia.pivottable.entities.ApplicationUser;
import ca.concordia.pivottable.entities.ShareableSchema;

public interface UserManagementService 
{
	/**
	 * Checks if the username and password chosen by a new application user are valid.
	 * @param	newUser	Application user object containing new user's credentials
	 * @return	Validation failure reason, if validation fails
	 * 			null, otherwise
	 */
	String validateNewUser(ApplicationUser newUser);
	
	/**
	 * Adds a new user's credentials (username in lower case and password hash) to the users database.
	 * @param	newUser	Application user object containing new user's credentials
	 * @return	true, if the user is successfully added
	 * 			false, otherwise
	 */
	boolean createUser(ApplicationUser newUser);
	
	/**
	 * Validates login credentials of an application user.
	 * @param 	existingUser	Application user object containing existing user's credentials
	 * @return	Validation failure reason, if validation fails
	 * 			null, otherwise
	 */
	String validateLogin(ApplicationUser existingUser);
	
	/**
	 * Adds a new shareable schema to the user database.
	 * @param 	shareableSchema	Details of the shareable schema
	 * @return	true, if schema is successfully added
	 * 			false, otherwise
	 */
	boolean createShareableSchema(ShareableSchema shareableSchema);
}
