package ca.concordia.pivottable.servicelayer.impl;

import java.io.IOException;
import ca.concordia.pivottable.entities.ApplicationConfiguration;
import ca.concordia.pivottable.servicelayer.ConfigurationHolder;
import ca.concordia.pivottable.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for retrieval of application configuration information. It implements the Singleton design pattern.
 * @author	Jyotsana Gupta
 * @version	1.0
 */
public class ConfigurationHolderSingleton implements ConfigurationHolder
{
	/**
	 * Used for logging information, warning and error messages during application run.
	 */
	private Logger log = LoggerFactory.getLogger(ApplicationConfiguration.class);
	
	/**
	 * Single instance of the Singleton configuration holder class.
	 */
	private static ConfigurationHolderSingleton singleConfigHolder;
	
	/**
	 * Class constructor.
	 */
	private ConfigurationHolderSingleton()
	{
		//empty default constructor
	}
	
	/**
	 * Ensures that only one instance of this class exists at all times.
	 * @return	The class instance
	 */
	public static ConfigurationHolderSingleton getConfigHolder()
	{
		if (singleConfigHolder == null)
			singleConfigHolder = new ConfigurationHolderSingleton();
		return singleConfigHolder;
	}
	
	/**
	 * Fetches the pivot table application configuration details for a user.
	 * @return	Object of ApplicationConfiguration class
	 */
	public ApplicationConfiguration getConfiguration(String configFilePath)
	{
		String json;
		try 
		{
			//Reading configuration details from a configuration file
			if (configFilePath == null)
				configFilePath = System.getProperty("user.dir") + "\\configuration.json";
			
			json = FileUtils.readFileContents(configFilePath);
			return ApplicationConfiguration.fromJSON(json);
		} 
		catch (IOException ioe) 
		{
			log.error("IOException occurred while reading configuration file... " + ioe.getMessage());
			log.info("Using default configuration details.");
			
			//Using default configuration details in case file read fails
			int serverPort = 4567;
			String dbUrl = "jdbc:mysql://localhost:3306/app_user_db?useSSL=false";
			String dbUsername = "root";
			String dbPassword = "root";
			
			ApplicationConfiguration appConfig = new ApplicationConfiguration(serverPort, dbUrl, dbUsername, dbPassword);
			return appConfig;
		}
	}
}