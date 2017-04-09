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
	// Some default values
	private static final String DEFAULT_DB_URL = "jdbc:mysql://localhost:3306/app_user_db?useSSL=false";
	private static final int DEFAULT_PORT = 4567;
	private static final String DEFAULT_DB_PWD = "root";
	private static final String DEFAULT_DB_USER = "root";

	// Property
	private static final String CONFIG_PROPERTY_KEY = "app.server.config.location";
	/**
	 * Used for logging information, warning and error messages during application run.
	 */
	private Logger log = LoggerFactory.getLogger(ApplicationConfiguration.class);
	
	/**
	 * Single instance of the Singleton configuration holder class.
	 */
	private static ConfigurationHolderSingleton singleConfigHolder;

	/**
	 * The configuration this holder is keeping. Note: it's final so it cannot be changed.
	 */
	final private ApplicationConfiguration appConfig;
	
	/**
	 * Class constructor.
	 */
	private ConfigurationHolderSingleton()
	{
		//empty default constructor
		appConfig = getInitialConfiguration();
	}
	
	/**
	 * Ensures that only one instance of this class exists at all times.
	 * @return	The class instance
	 */
	public static synchronized ConfigurationHolder getConfigHolder()
	{
		if (singleConfigHolder == null)
			singleConfigHolder = new ConfigurationHolderSingleton();
		return singleConfigHolder;
	}

	public ApplicationConfiguration getConfiguration() {
		return this.appConfig;
	}
	
	/**
	 * Fetches the pivot table application configuration details for a user.
	 * @return	Object of ApplicationConfiguration class
	 */
	private ApplicationConfiguration getInitialConfiguration()
	{
		String json;
		try 
		{
			//Reading configuration details from a configuration file
			String	configFilePath = System.getProperty(CONFIG_PROPERTY_KEY);
			if (configFilePath == null) {
				configFilePath = "configuration.json";
			}
			
			json = FileUtils.readFileContents(configFilePath);
			ApplicationConfiguration appConf = ApplicationConfiguration.fromJSON(json);
			if (appConf.getAppDatabasePassword() == null) {
				log.info("Missing app db password. Using default.");
				appConf.setAppDatabasePassword(DEFAULT_DB_PWD);
			}
			if (appConf.getAppDatabaseUrl() == null) {
				log.info("Missing app db url. Using default.");
				appConf.setAppDatabaseUrl(DEFAULT_DB_URL);
			}
			if (appConf.getAppDatabaseUser() == null) {
				log.info("Missing app db user. Using default.");
				appConf.setAppDatabaseUser(DEFAULT_DB_USER);
			}
			if (appConf.getAppServerPort() == null) {
				log.info("Missing app server port. Using default.");
				appConf.setAppServerPort(DEFAULT_PORT);
			}
			return appConf;
		} 
		catch (IOException ioe) 
		{
			log.error("IOException occurred while reading configuration file... " + ioe.getMessage());
			log.info("Using default configuration details.");
			
			//Using default configuration details in case file read fails
			return new ApplicationConfiguration(DEFAULT_PORT, DEFAULT_DB_URL, DEFAULT_DB_USER, DEFAULT_DB_PWD);
		}
	}
}
