package ca.concordia.pivottable.servicelayer;

import ca.concordia.pivottable.entities.ApplicationConfiguration;

/**
 * Interface for retrieval of application configuration information.
 * @author	Jyotsana Gupta
 * @version	1.0
 */
public interface ConfigurationHolder 
{
	/**
	 * Fetches the pivot table application configuration details for a user.
	 * @return	Object of ApplicationConfiguration class
	 */
	ApplicationConfiguration getConfiguration();
}
