package ca.concordia.pivottable.servicelayer.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;
import ca.concordia.pivottable.datalayer.UserDataAccess;
import ca.concordia.pivottable.datalayer.impl.UserDataAccessImpl;
import ca.concordia.pivottable.entities.ApplicationUser;
import ca.concordia.pivottable.entities.ShareableSchema;
import ca.concordia.pivottable.servicelayer.UserManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserManagementServiceImpl implements UserManagementService
{
	/**
	 * Used for logging information, warning and error messages during application run.
	 */
	private Logger log = LoggerFactory.getLogger(UserManagementServiceImpl.class);
	
	/**
	 * User database object used for performing data processing operations.
	 */
	private UserDataAccess userDatabase = new UserDataAccessImpl();
	
	/**
	 * Checks if the username and password chosen by a new application user are valid.
	 * @param	newUser	Application user object containing new user's credentials
	 * @return	Validation failure reason, if validation fails
	 * 			null, otherwise
	 */
	public String validateNewUser(ApplicationUser newUser)
	{
		String userValidation = null;
		String username = newUser.getUsername().toLowerCase();
		String password = newUser.getPassword();
		
		if ((username == null) || (username.trim().isEmpty()))
			userValidation = "Username is either blank or contains only whitespaces.";
		else if ((password == null) || (password.trim().isEmpty()))
			userValidation = "Password is either blank or contains only whitespaces.";
		else
		{
			userDatabase.connect();
			boolean userExists = userDatabase.usernameExists(username);
			if (userExists)
				userValidation = "Username already exists.";
		}
			
		return userValidation;
	}
	
	/**
	 * Adds a new user's credentials (username in lower case and password hash) to the users database.
	 * @param	newUser	Application user object containing new user's credentials
	 * @return	true, if the user is successfully added
	 * 			false, otherwise
	 */
	public boolean createUser(ApplicationUser newUser)
	{
		String username = newUser.getUsername().toLowerCase();
		String password = newUser.getPassword();		
		
		//Hashing the user password
		String passwordHash = hashPassword(password);
		
		//Adding the new user to the user database
		userDatabase.connect();
		return userDatabase.addUser(username, passwordHash);
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
	 * @return	Validation failure reason, if validation fails
	 * 			null, otherwise
	 */
	public String validateLogin(ApplicationUser existingUser)
	{
		String username = existingUser.getUsername().toLowerCase();
		String password = existingUser.getPassword();	
		
		//Hashing the user password
		String passwordHash = hashPassword(password);
		
		//Validating the user login credentials
		String userValidation = null;
		if ((username == null) || (username.trim().isEmpty()))
			userValidation = "Username is either blank or contains only whitespaces.";
		else if ((password == null) || (password.trim().isEmpty()))
			userValidation = "Password is either blank or contains only whitespaces.";
		else
		{
			userDatabase.connect();
			boolean userExists = userDatabase.usernameExists(username);
			if (!userExists)
				userValidation = "Username does not exist.";
			else
			{
				String userPasswordHash = userDatabase.getUserPasswordHash(username);
				if (userPasswordHash == null)
					userValidation = "Unable to validate password due to database connectivity issue.";
				else if (!passwordHash.equals(userPasswordHash))
					userValidation = "Password is incorrect.";
			}
		}
		
		return userValidation;
	}
	
	/**
	 * Adds a new shareable schema to the user database.
	 * @param 	shareableSchema	Details of the shareable schema
	 * @return	true, if schema is successfully added
	 * 			false, otherwise
	 */
	public boolean createShareableSchema(ShareableSchema shareableSchema)
	{
		//Parsing shareable schema object
		String schemaName = shareableSchema.getSchemaName();
		String ownerUsername = shareableSchema.getOwnerUsername();
		String dbURL = shareableSchema.getDbURL();
		String dbUsername = shareableSchema.getDbUsername();
		String dbPassword = shareableSchema.getDbPassword();
		String pvtTblSchema = shareableSchema.getPvtTblSchema().toJSON();
		
		//Adding the new shareable schema to the user database
		userDatabase.connect();
		return userDatabase.addShareableSchema(schemaName, ownerUsername, dbURL, dbUsername, dbPassword, pvtTblSchema);
	}
}
