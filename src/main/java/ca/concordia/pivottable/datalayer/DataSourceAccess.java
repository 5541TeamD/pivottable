package ca.concordia.pivottable.datalayer;

import java.util.List;

/**
 * Defines the interface for data source access operations.
 * @author 	Jyotsana Gupta
 * @version	1.0
 */
public interface DataSourceAccess 
{
	/**
	 * Assigns the specific strategy to be used for pivot table data retrieval.
	 * @param	pvtTblStrategy	Specific strategy object
	 */
	void setPvtTblStrategy(PivotTableStrategy pvtTblStrategy);
	
	/**
	 * Sets the credentials required for connecting to a database.
	 * @param	dbUrl		URL of the database
	 * @param	dbUsername	Username for login
	 * @param	dbPassword	Password for login
	 */
	void setCredentials(String dbUrl, String dbUsername, String dbPassword);
	
	/**
	 * Initiates a connection with the data source.
	 */
	void connect();
	
	/**
	 * Closes the connection with the data source.
	 * @return	true, if connection is closed successfully, or if the connection was already closed
	 * <br>		false, if the attempt to disconnect fails
	 */
    boolean disconnect();
	
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
  	 * Executes SQL query on the database and fetches all the values of the selected page label column 
  	 * @param	pageLabel		Page label column selected as part of pivot table schema
  	 * @param	tableName		Raw report table name
  	 * @return	List of page label column values
  	 */
	List<String> getPageLabelValues(String pageLabel, String tableName, String filterField, String filterValue,
									String sortField, String sortOrder);
	
	/**
	 * Retrieves pivot table data based on the strategy assigned.
	 * @return	Pivot table data
	 */
	List<List<List<Object>>> executePvtTblStrategy();
}
