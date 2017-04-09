package ca.concordia.pivottable.servicelayer.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;
import ca.concordia.pivottable.datalayer.UserDataAccess;
import ca.concordia.pivottable.datalayer.impl.UserDataAccessImpl;
import ca.concordia.pivottable.entities.ApplicationUser;
import ca.concordia.pivottable.servicelayer.UserManagementService;
import ca.concordia.pivottable.utils.PivotTableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles application user management operations, including user creation, validation and password hashing.
 * @author	Jyotsana Gupta
 * @version	1.0
 */
public class UserManagementServiceImpl implements UserManagementService
{
	/**
	 * Used for logging information, warning and error messages during application run.
	 */
	private Logger log = LoggerFactory.getLogger(UserManagementServiceImpl.class);
	
	/**
	 * User database object used for performing data processing operations.
	 */
	private UserDataAccess userDatabase;

	/**
	 * Constructor provides the UserDataAccess implementation
	 * @param userDatabase The implementation for connecting to the user database class.
	 */
	public UserManagementServiceImpl(UserDataAccess userDatabase) {
		this.userDatabase = userDatabase;
	}

	/**
	 * Adds a new user's credentials (username in lower case and password hash) to the users database.
	 * @param	newUser	Application user object containing new user's credentials
	 */
	public void createUser(ApplicationUser newUser)
	{
		//Validating new user before creation
		String userValidation = validateNewUser(newUser);
		
		if (userValidation != null)
			throw new PivotTableException("Unable to validate new user before creation. " + userValidation);
		else
		{
			//Parsing the application user object
			String username = newUser.getUsername().toLowerCase();
			String password = newUser.getPassword();
			
			//Hashing the user password
			String passwordHash = hashPassword(password);
			
			//Adding the new user to the user database
			try 
			{
				userDatabase.connect();
				boolean userAdded = userDatabase.addUser(username, passwordHash);
	
				//Throwing an exception to the UI in case user creation fails
				if (!userAdded)
					throw new PivotTableException("Unable to create new user " + username + ".");
			} 
			finally 
			{
				userDatabase.disconnect();
			}
		}
	}
	
	/**
	 * Checks if the username and password chosen by a new application user are valid.
	 * @param	newUser	Application user object containing new user's credentials
	 * @return	User validation error message, if user validation fails
	 * 			null, otherwise
	 */
	private String validateNewUser(ApplicationUser newUser)
	{
		String userValidation = null;
		
		//Parsing the application user object
		String username = newUser.getUsername();
		String password = newUser.getPassword();
		
		if ((username == null) || (username.trim().isEmpty()))
			userValidation = "Username is either blank or contains only whitespaces.";
		else if ((password == null) || (password.trim().isEmpty()))
			userValidation = "Password is either blank or contains only whitespaces.";
		else
		{
			username = username.toLowerCase();
			try 
			{
				userDatabase.connect();
				boolean userExists = userDatabase.usernameExists(username);
				if (userExists)
					userValidation = "Username " + username + " already exists.";
			} 
			finally 
			{
				userDatabase.disconnect();
			}
		}
			
		return userValidation;
	}
	
	/**
	 * Hashes password using SHA-256 algorithm.
	 * @param 	password	Password string to be hashed
	 * @return	String containing the password hash
	 */
	private String hashPassword(String password)
	{
		byte[] hash = null;
		
		try
		{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			hash = digest.digest(password.getBytes("UTF-8"));
		}
		catch(NoSuchAlgorithmException nsae)
		{
			log.error("Exception occurred while hashing the user password... " + nsae.getMessage());
		}
		catch(UnsupportedEncodingException uee)
		{
			log.error("Exception occurred while hashing the user password... " + uee.getMessage());
		}
		
		return DatatypeConverter.printHexBinary(hash);
	}
	
	/**
	 * Validates login credentials of an application user.
	 * @param 	existingUser	Application user object containing existing user's credentials
	 */
	public void validateLogin(ApplicationUser existingUser)
	{
		String userValidation = null;
		String username = existingUser.getUsername();
		String password = existingUser.getPassword();
		
		//Validating the user login credentials
		if ((username == null) || (username.trim().isEmpty()))
			userValidation = "Username is either blank or contains only whitespaces.";
		else if ((password == null) || (password.trim().isEmpty()))
			userValidation = "Password is either blank or contains only whitespaces.";
		else
		{
			username = username.toLowerCase();
			
			//Hashing the user password
			String passwordHash = hashPassword(password);
			
			try {
				userDatabase.connect();
				boolean userExists = userDatabase.usernameExists(username);
				if (!userExists)
					userValidation = "Username " + username + " does not exist.";
				else {
					String userPasswordHash = userDatabase.getUserPasswordHash(username);
					if (userPasswordHash == null)
						userValidation = "Unable to validate password due to database connectivity issue.";
					else if (!passwordHash.equals(userPasswordHash))
						userValidation = "Password is incorrect.";
				}
			} finally {
				userDatabase.disconnect();
			}
		}
		
		//Throwing an exception to the UI in case any validation fails
		if (userValidation != null)
			throw new PivotTableException(userValidation);
	}
	
	/**
	 * Deletes an existing user from the users database.
	 * @param	username	Username of the user to be deleted
	 */
	public void deleteUser(String username)
	{
		boolean userDeleted = false;
		
		//Deleting the user record from the user database
		try {
			userDatabase.connect();
			boolean userExists = userDatabase.usernameExists(username);
			if (!userExists)
				userDeleted = false;
			else
				userDeleted = userDatabase.deleteUser(username);

			//Throwing an exception to the UI in case user deletion fails
			if (!userDeleted)
				throw new PivotTableException("Unable to delete user " + username + ".");
		} finally {
			userDatabase.disconnect();
		}
	}
}
