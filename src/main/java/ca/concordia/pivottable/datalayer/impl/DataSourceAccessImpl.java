package ca.concordia.pivottable.datalayer.impl;

import ca.concordia.pivottable.datalayer.DataSourceAccess;
import ca.concordia.pivottable.datalayer.PivotTableStrategy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles all the operations to be performed on the pivot table database.
 * @author	Jyotsana Gupta
 * @version 1.0
 */
public class DataSourceAccessImpl implements DataSourceAccess
{

	/**
	 * This implementation limits the number of results to a 1000
	 */
	private static final int ROW_LIMIT = 1000;

	/**
	 * URL of the database to be connected to.
	 */
	private String dbUrl;
	
	/**
	 * Username to login to the database.
	 */
	private String dbUsername;
	
	/**
	 * Password to login to the database.
	 */
	private String dbPassword;
	
	/**
	 * Database connection object
	 */
	Connection dbConnection = null;
	
	/**
	 * Strategy to retrieve pivot table data.
	 */
	private PivotTableStrategy pvtTblStrategy = null;
	
	/**
	 * Used for logging information, warning and error messages during application run.
	 */
	private Logger log = LoggerFactory.getLogger(DataSourceAccessImpl.class);
	
	/**
	 * Assigns the specific strategy to be used for pivot table data retrieval.
	 * @param	pvtTblStrategy	Specific strategy object
	 */
	public void setPvtTblStrategy(PivotTableStrategy pvtTblStrategy)
	{
		this.pvtTblStrategy = pvtTblStrategy;
	}
	
	/**
	 * Sets the credentials required for connecting to a database.
	 * @param	dbUrl		URL of the database
	 * @param	dbUsername	Username for login
	 * @param	dbPassword	Password for login
	 */
	public void setCredentials(String dbUrl, String dbUsername, String dbPassword)
	{
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }
	
	/**
	 * Initiates a connection with the data source.
	 */
	public void connect()
	{
		//Database driver required for connection
		String jdbcDriver = "com.mysql.jdbc.Driver";
		
		try
		{
			//Loading JDBC Driver class at run-time
			Class.forName(jdbcDriver);
		}
		catch (Exception dbConnGenExcpn)
		{
			log.error("Unexpected exception occurred while attempting DB connection... " + dbConnGenExcpn.getMessage());
		}		
		
		log.info("Initiating connection to database " + dbUrl + "...");
		
		try
		{
			dbConnection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		}
		catch (SQLException dbConnSQLExcpn)
		{
			dbConnection = null;
			log.error("SQLException occurred while connecting to database... " + dbConnSQLExcpn.getMessage());
		}
	}
	
	/**
	 * Closes the connection with the data source.
	 * @return	true, if connection is closed successfully, or if the connection was already closed
	 * <br>		false, if the attempt to disconnect fails
	 */
    public boolean disconnect()
    {
    	if (dbConnection != null)
    	{
    		try
    		{
    			dbConnection.close();
    		}
    		catch (SQLException dbDisconnSQLExcpn)
    		{
    			log.error("SQLException occurred while disconnecting from database... " + dbDisconnSQLExcpn.getMessage());
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    /**
     * Tests the connection to the data source by first connecting and then disconnecting.
     * @return	true, if connection test is successful
     * 			false, if connection test fails
     */
    public boolean testConnection()
    {
    	connect();
    	
    	if (dbConnection != null)
    	{
    		return disconnect();
    	}
    	
    	return false;
    }
    
    /**
     * Fetches the names of all available raw report tables from the database. 
     * @return	List of all available raw report table names
     * 			null, if database connection fails
     */
  	public List<String> getAllRawTableNames()
  	{
  		
  		if (dbConnection == null)							//failed connection
  		{
  			return null;
  		}
  		
  		//Proceeding, if database connection is successful
  		ResultSet rsAllRawTblNames = null;
  		Statement stmt = null;
  		List<String> allRawTblList = new ArrayList<String>();
  		
  		try
  		{
  			//Fetching all table names from database
  			//For MySQL database
  			if (dbUrl.indexOf("mysql") >= 0)
  			{
  				DatabaseMetaData dbmd = dbConnection.getMetaData();
  	  			rsAllRawTblNames = dbmd.getTables(null, null, "%", null);
  			}
  			//For PostgreSQL database
  			else if (dbUrl.indexOf("postgresql") >= 0)
  			{
  				String allRawTblNamesQuery = "SELECT * FROM information_schema.tables WHERE table_schema = \'public\';";
  				stmt = dbConnection.createStatement();
  				rsAllRawTblNames = stmt.executeQuery(allRawTblNamesQuery);
  			}
  			  			
  			//Creating a list of table names
  			while (rsAllRawTblNames.next())
  			{
  				allRawTblList.add(rsAllRawTblNames.getString("TABLE_NAME"));
  			}
  			
  			rsAllRawTblNames.close();
  			if (stmt != null)
  				stmt.close();
  		}
  		catch (SQLException allRawTblSQLExcpn)
  		{
  			rsAllRawTblNames = null;
  			stmt = null;
  			log.error("SQLException occurred while fetching all raw table names from database... " + allRawTblSQLExcpn.getMessage());
  		}
  		  		
  		return allRawTblList;
  	}
  	
  	/**
  	 * Fetches all the data stored in a table in the database.
  	 * @param	tableName	Name of the table whose data needs to be fetched
  	 * @return	All the data stored in the table.
  	 * 			null, if database connection fails
  	 */
  	public List<List<Object>> getTableData(String tableName)
  	{
  		
  		if (dbConnection == null)							//failed connection
  		{
  			return null;
  		}
  		
  		//Proceeding, if database connection is successful
  		Statement stmtTblData = null;
  		ResultSet rsTblData = null;
  		ResultSetMetaData rsmdTblData = null;
  		int fieldCount = 0;
  		List<List<Object>> tblData = new ArrayList<List<Object>>();
  		
  		//Executing SQL query to get all the data of the table
  		String tblDataQuery = "SELECT * FROM " + tableName + " LIMIT " + String.valueOf(ROW_LIMIT) + ";";
  		try
  		{
  			stmtTblData = dbConnection.createStatement();
  			log.info("Running query " + tblDataQuery);
  			rsTblData = stmtTblData.executeQuery(tblDataQuery);
  			
  			//Fetching field count for the results returned by the SQL query executed
  			rsmdTblData = rsTblData.getMetaData();
  			fieldCount = rsmdTblData.getColumnCount();
  			
  			while (rsTblData.next())
  			{
  				List<Object> tblRecord = new ArrayList<Object>();
  				
  				for (int i=1; i<=fieldCount; i++)
  				{
  					tblRecord.add(rsTblData.getObject(i));
  				}
  				
  				tblData.add(tblRecord);
  			}

  			rsTblData.close();
  			stmtTblData.close();
  		}
  		catch (SQLException allTblDataSQLExcpn)
  		{
  			stmtTblData = null;
  			rsTblData = null;
  			rsmdTblData = null;
  			tblData = null;
  			log.error("SQLException occurred while fetching all the data of table " + tableName + "... " + allTblDataSQLExcpn.getMessage());
  		}
  		
  		return tblData;
  	}
  	
  	/**
  	 * Fetches names and data types of data fields belonging to a table from the database.
  	 * @param	tableName	Name of the table whose field details need to be fetched
  	 * @return	List of all data field names and types belonging to the table.
  	 * 			Each element of the list is a String array with index 0 holding the field name 
  	 * 			and index 1 holding the corresponding type
  	 * 			null, if database connection fails
  	 */
  	public List<String[]> getTableFields(String tableName)
  	{
  		
  		if (dbConnection == null)							//failed connection
  		{
  			return null;
  		}
  		
  		//Proceeding, if database connection is successful
  		Statement stmtTblFields = null;
  		ResultSet rsTblFields = null;
  		ResultSetMetaData rsmdTblFields = null;
  		int fieldCount = 0;
  		List<String[]> tblFields = new ArrayList<String[]>();
  		
  		//Executing SQL query to get 1 row of the table
  		String tblDataQuery = "SELECT * FROM " + tableName + " LIMIT 1;";
  		try
  		{
  			stmtTblFields = dbConnection.createStatement();
  			log.info("Running query " + tblDataQuery);
  			rsTblFields = stmtTblFields.executeQuery(tblDataQuery);
  			
  			//Fetching field count for the results returned by the SQL query executed
  			rsmdTblFields = rsTblFields.getMetaData();
  			fieldCount = rsmdTblFields.getColumnCount();
  			
  			//Fetching field names and adding them to the list
  			for (int i=1; i<=fieldCount; i++)
  			{
  				String[] dataField = new String[2];
  				
  				dataField[0] = rsmdTblFields.getColumnName(i);				//field name
  				dataField[1] = rsmdTblFields.getColumnTypeName(i);			//field data type
  				
  				//Categorizing field type into "string" and "numeric" types  
  				if (dataField[1].toUpperCase().indexOf("CHAR") >= 0)
  				{
  					dataField[1] = "string";
  				}
  				else
  				{
  					dataField[1] = "numeric";
  				}
  				
  				tblFields.add(dataField);
  			}
  			
  			rsTblFields.close();
  			stmtTblFields.close();
  		}
  		catch (SQLException tblFieldsSQLExcpn)
  		{
  			stmtTblFields = null;
  			rsTblFields = null;
  			rsmdTblFields = null;
  			tblFields = null;
  			log.error("SQLException occurred while fetching field details of table " + tableName + "... " + tblFieldsSQLExcpn.getMessage());
  		}
  		
  		return tblFields;
  	}
  	
  	/**
  	 * Executes SQL query on the database and fetches all the values of the selected page label column 
  	 * @param	pageLabel		Page label column selected as part of pivot table schema
  	 * @param	tableName		Raw report table name
  	 * @return	List of page label column values
  	 */
  	public List<String> getPageLabelValues(String pageLabel, String tableName,
										   String filterField, String filterValue,
										   String sortField, String sortOrder)
  	{
  		
  		if (dbConnection == null)							//failed connection
  		{
  			return null;
  		}
  		
  		//Proceeding, if database connection is successful
  		String pageLabelQuery = null;
  		Statement stmtPageLabels = null;
  		ResultSet rsPageLabels = null;
  		List<String> pageLabelValues = new ArrayList<String>();

  		String orderByClause = pageLabel.equals(sortField) ? " ORDER BY " + sortField + " " + sortOrder : "";
  		String whereClause = "";
  		if (filterField != null && !filterField.trim().isEmpty() && filterValue != null && !filterField.trim().isEmpty()) {
  			whereClause = " WHERE " + filterField + " = \'" + filterValue + "\';";
		}
  		//Generating the SQL query to get all the values of the selected page label column
		pageLabelQuery = "SELECT DISTINCT " + pageLabel
							+ " FROM (SELECT * FROM " + tableName + " LIMIT " + String.valueOf(ROW_LIMIT) + ") as sublist "
							+ whereClause + orderByClause;
		
		//Executing the page label SQL query
  		try
  		{
  			stmtPageLabels = dbConnection.createStatement();
  			log.info("Running query " + pageLabelQuery);
  			rsPageLabels = stmtPageLabels.executeQuery(pageLabelQuery);
  			
  			while (rsPageLabels.next())
  				pageLabelValues.add(rsPageLabels.getString(pageLabel));
  			
  			rsPageLabels.close();
  			stmtPageLabels.close();
  		}
  		catch (SQLException pageLabelsSQLExcpn)
  		{
  			stmtPageLabels = null;
  			rsPageLabels = null;
  			pageLabelValues = null;
  			log.error("SQLException occurred while fetching page label values... " + pageLabelsSQLExcpn.getMessage());
  		}
  		
  		return pageLabelValues;
  	}
  	
  	/**
	 * Retrieves pivot table data based on the strategy assigned.
	 * @return	Pivot table data
	 */
	public List<List<List<Object>>> executePvtTblStrategy()
	{
		return pvtTblStrategy.getPvtTblData(dbConnection, ROW_LIMIT);
	}
}
