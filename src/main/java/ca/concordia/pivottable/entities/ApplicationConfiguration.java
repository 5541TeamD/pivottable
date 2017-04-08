package ca.concordia.pivottable.entities;

import com.google.gson.Gson;

/**
 * Class for managing pivot table application connection configuration details for the user.
 */
public class ApplicationConfiguration 
{
	private Integer appServerPort;
	private String appDatabaseUrl;
	private String appDatabaseUser;
	private String appDatabasePassword;
	
	public ApplicationConfiguration() 
	{
        //empty default constructor
    }
	
	/**
	 * Constructor with all the parameters provided.
	 * @param 	appServerPort			Application server port
	 * @param 	appDatabaseUrl			Application database URL
	 * @param 	appDatabaseUser			Application database login username
	 * @param 	appDatabasePassword		Application database login password
	 */
	public ApplicationConfiguration(int appServerPort, String appDatabaseUrl, String appDatabaseUser, String appDatabasePassword)
	{
		this.appServerPort = appServerPort;
		this.appDatabaseUrl = appDatabaseUrl;
		this.appDatabaseUser = appDatabaseUser;
		this.appDatabasePassword = appDatabasePassword;
	}

	/**
	 * Accessor method for the application server port.
	 * @return	appServerPort of this application
	 */
	public Integer getAppServerPort()
	{
		return appServerPort;
	}

	/**
	 * Accessor method for the application database URL.
	 * @return	appDatabaseUrl of this application
	 */
	public String getAppDatabaseUrl() 
	{
		return appDatabaseUrl;
	}

	/**
	 * Accessor method for the application database username.
	 * @return	appDatabaseUser of this application
	 */
	public String getAppDatabaseUser() 
	{
		return appDatabaseUser;
	}

	/**
	 * Accessor method for the application database password.
	 * @return	appDatabasePassword of this application
	 */
	public String getAppDatabasePassword() 
	{
		return appDatabasePassword;
	}

	/**
	 * Mutator method for the application server port.
	 * @param	appServerPort	Server port to be used
	 */
	public void setAppServerPort(int appServerPort) 
	{
		this.appServerPort = appServerPort;
	}

	/**
	 * Mutator method for the application database URL.
	 * @param	appDatabaseUrl	URL of the database to be used
	 */
	public void setAppDatabaseUrl(String appDatabaseUrl) 
	{
		this.appDatabaseUrl = appDatabaseUrl;
	}

	/**
	 * Mutator method for the application database username.
	 * @param	appDatabaseUser	Login username of the database to be used
	 */
	public void setAppDatabaseUser(String appDatabaseUser) 
	{
		this.appDatabaseUser = appDatabaseUser;
	}

	/**
	 * Mutator method for the application database password.
	 * @param	appDatabasePassword	Login password of the database to be used
	 */
	public void setAppDatabasePassword(String appDatabasePassword) 
	{
		this.appDatabasePassword = appDatabasePassword;
	}
	
	/**
     * Creates an instance of ApplicationConfiguration from a JSON string.
     * @param	json	The string containing the JSON: note, the server returns it in a data object
     * @return	An ApplicationConfiguration object
     */
    public static ApplicationConfiguration fromJSON(String json)
    {
        Gson gson = new Gson();
        return gson.fromJson(json, ApplicationConfiguration.class);
    }
}
