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
  	 * Executes SQL query on database and fetches pivot table data without page label.
  	 * @param	rowLabels	List of row labels selected as part of pivot table schema
  	 * @param	colLabels	List of column labels selected as part of pivot table schema
  	 * @param	function	Mathematical function selected as part of pivot table schema
  	 * @param	valField	Value field selected as part of pivot table schema
  	 * @param	filterField	Field name by which pivot table data needs to be filtered
  	 * @param	filterValue	Value of the filter field for which pivot table data needs to be displayed
  	 * @param	sortField	Field name by which pivot table data needs to be sorted
  	 * @param	sortOrder	Order (ascending/descending) in which pivot table data needs to be sorted
  	 * @param	tableName	Raw report table name
  	 * @return	Pivot table data fetched from the database
  	 */
  	List<List<List<Object>>> getPvtTblData(List<String> rowLabels, List<String> colLabels, String function, String valField, 
  											String filterField, String filterValue, String sortField, String sortOrder, String tableName);
	
  	/**
  	 * Executes SQL query on database and fetches pivot table data with page label.
  	 * @param	rowLabels	Row labels selected as part of pivot table schema
  	 * @param	colLabels	Column labels selected as part of pivot table schema
  	 * @param	pageLabel	Page label selected as part of pivot table schema
  	 * @param	function	Mathematical function selected as part of pivot table schema
  	 * @param	valField	Value field selected as part of pivot table schema
  	 * @param	filterField	Field name by which pivot table data needs to be filtered
  	 * @param	filterValue	Value of the filter field for which pivot table data needs to be displayed
  	 * @param	sortField	Field name by which pivot table data needs to be sorted
  	 * @param	sortOrder	Order (ascending/descending) in which pivot table data needs to be sorted
  	 * @param	tableName	Raw report table name
  	 * @return	Pivot table data fetched from the database
  	 */
  	public List<List<List<Object>>> getPvtTblData(List<String> rowLabels, List<String> colLabels, String pageLabel, String function, String valField, 
  													String filterField, String filterValue, String sortField, String sortOrder, String tableName);
  	
	/**
  	 * Executes SQL query on the database and fetches all the values of the selected page label column 
  	 * @param	pageLabel		Page label column selected as part of pivot table schema
  	 * @param	tableName		Raw report table name
  	 * @return	List of page label column values
  	 */
	List<String> getPageLabelValues(String pageLabel, String tableName);
}
