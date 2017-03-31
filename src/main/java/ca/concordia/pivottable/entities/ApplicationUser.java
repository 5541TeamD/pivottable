package ca.concordia.pivottable.entities;

/**
 * POJO representing an application user.
 */
public class ApplicationUser 
{
	private final String username;
	private final String passwordHash;
	
	/**
	 * Constructor with all parameters provided. 
	 * @param 	username		Username for the user to login to the pivot table application
	 * @param 	passwordHash	Hash of password for the user to login to the pivot table application
	 */
	public ApplicationUser(String username, String passwordHash)
	{
		this.username = username;
		this.passwordHash = passwordHash;
	}

	/**
	 * Accessor method for data member username.
	 * @return	username of this application user
	 */
	public String getUsername() 
	{
		return this.username;
	}

	/**
	 * Accessor method for data member passwordHash.
	 * @return	passwordHash of this application user
	 */
	public String getPasswordHash() 
	{
		return this.passwordHash;
	}
}
