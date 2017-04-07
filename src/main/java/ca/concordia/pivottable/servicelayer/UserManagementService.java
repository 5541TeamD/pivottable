package ca.concordia.pivottable.servicelayer;

import ca.concordia.pivottable.entities.ApplicationUser;

/**
 * Defines the interface for pivot table application user management service operations.
 * @author 	Jyotsana Gupta
 * @version	1.0
 */
public interface UserManagementService 
{	
	/**
	 * Adds a new user's credentials (username in lower case and password hash) to the users database.
	 * @param	newUser	Application user object containing new user's credentials
	 */
	void createUser(ApplicationUser newUser);
	
	/**
	 * Validates login credentials of an application user.
	 * @param 	existingUser	Application user object containing existing user's credentials
	 */
	void validateLogin(ApplicationUser existingUser);
	
	/**
	 * Deletes an existing user from the users database.
	 * @param	username	Username of the user to be deleted
	 */
	void deleteUser(String username);
}
