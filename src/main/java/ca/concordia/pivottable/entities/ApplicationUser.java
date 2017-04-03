package ca.concordia.pivottable.entities;

/**
 * POJO representing an application user.
 */
public class ApplicationUser 
{
	private final String username;
	private final String password;
	
	/**
	 * Constructor with all parameters provided. 
	 * @param 	username		Username for the user to login to the pivot table application
	 * @param 	password		Password for the user to login to the pivot table application
	 */
	public ApplicationUser(String username, String password)
	{
		this.username = username;
		this.password = password;
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
	 * Accessor method for data member password.
	 * @return	password of this application user
	 */
	public String getPassword() 
	{
		return this.password;
	}
}
