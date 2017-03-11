package ca.concordia.pivottable.datalayer;

import java.util.List;
import ca.concordia.pivottable.entities.PivotTableSchema;

/**
 * Defines the interface for data source access operations.
 * @author Jyotsana Gupta
 * @version 1.0
 */
public interface DataSourceAccess 
{
	/**
	 * Sets the credentials required for connecting to a database.
	 * @param	dbUrl		URL of the database
	 * @param	dbUsername	Username for login
	 * @param	dbPassword	Password for login
	 */
	void setCredentials(String dbUrl, String dbUsername, String dbPassword);
	
	/**
     * Tests the connection to the data source by first connecting and then disconnecting.
     * @return	true, if connection test is successful
     * 			false, if connection test fails
     */
	boolean testConnection();
	
	/**
     * Fetches the names of all available raw report tables from the database. 
     * @return	List of all available raw report table names
     * 			null, if database connection fails
     */
	List<String> getAllRawTableNames();
	
	/**
  	 * Checks if a table exists in the database.
  	 * @param	tableName	Name of the table whose existence needs to be verified
  	 * @return	true, if the table exists in the database
  	 * 			false, if the table does not exist in the database or database connection fails
  	 */
	boolean tableExists(String tableName);
	
	/**
  	 * Fetches all the data stored in a table in the database.
  	 * @param	tableName	Name of the table whose data needs to be fetched
  	 * @return	All the data stored in the table.
  	 * 			null, if database connection fails
  	 */
	List<List<Object>> getTableData(String tableName);
	
	/**
  	 * Fetches names and data types of data fields belonging to a table from the database.
  	 * @param	tableName	Name of the table whose field details need to be fetched
  	 * @return	List of all data field names and types belonging to the table.
  	 * 			Each element of the list is a String array with index 0 holding the field name 
  	 * 			and index 1 holding the corresponding type
  	 * 			null, if database connection fails
  	 */
	List<String[]> getTableFields(String tableName);
	
	/**
  	 * Fetches pivot table data from the database according to the input schema.
  	 * @param	pvtTblSchema	Schema defined for pivot table
  	 * @return	Pivot table data
  	 * 			null, if database connection fails
  	 */
	List<List<Object>> getPivotTableData(PivotTableSchema pvtTblSchema);
}
