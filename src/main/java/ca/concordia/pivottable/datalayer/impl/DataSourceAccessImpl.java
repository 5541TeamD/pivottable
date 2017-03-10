package ca.concordia.pivottable.datalayer.impl;

import ca.concordia.pivottable.datalayer.DataSourceAccess;
import ca.concordia.pivottable.entities.PivotTableSchema;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles all the operations to be performed on the data source.
 * At present, there is only one database that is accessed by this class.
 * @author Jyotsana Gupta
 * @version 1.0
 */
public class DataSourceAccessImpl implements DataSourceAccess 
{
	/**
	 * Used for logging information, warning and error messages during application run.
	 */
	private Logger log = LoggerFactory.getLogger(DataSourceAccessImpl.class);
	
	/**
	 * Initiates a connection with the data source.
	 * @return	An object of type Connection (holding connection details), if connection is successful
	 * <br>		null, if connection fails
	 */
	private Connection connect()
	{
		//Database attributes required for connection
		String jdbcDriver = "com.mysql.jdbc.Driver";
		String dbName = "testdb";
		String dbUrl = "jdbc:mysql://localhost:3306/" + dbName + "?useSSL=false";
		String dbUsername = "root";
		String dbPassword = "root";
		
		//Database connection state
		Connection dbConnection = null;
		
		try
		{
			//Loading JDBC Driver class at run-time
			Class.forName(jdbcDriver);
		}
		catch (Exception dbConnGenExcpn)
		{
			log.error("Unexpected exception occurred while attempting DB connection... " + dbConnGenExcpn.getMessage());
			
		}		
		
		log.info("Initiating connection to database " + dbName + "...");
		
		try
		{
			dbConnection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		}
		catch (SQLException dbConnSQLExcpn)
		{
			dbConnection = null;
			log.error("SQLException occurred while connecting to database... " + dbConnSQLExcpn.getMessage());
		}
		
		return dbConnection;
	}
	
	/**
	 * Closes the connection with the data source.
	 * @param	dbConnection	An object of type Connection referring to the data source connection to be closed.
	 * @return	true, if connection is closed successfully, or if the connection was already closed
	 * <br>		false, if the attempt to disconnect fails
	 */
    private boolean disconnect(Connection dbConnection)
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
    	Connection testConnection = connect();
    	
    	if (testConnection != null)
    	{
    		return disconnect(testConnection);
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
  		//Connecting to data base
  		Connection dbConnection = connect();
  		
  		if (dbConnection == null)							//failed connection
  		{
  			return null;
  		}
  		
  		//Proceeding, if database connection is successful
  		DatabaseMetaData dbmd = null;
  		ResultSet rsAllRawTblNames = null;
  		List<String> allRawTblList = new ArrayList<String>();
  		
  		try
  		{
  			//Fetching all table names from database
  			dbmd = dbConnection.getMetaData();
  			rsAllRawTblNames = dbmd.getTables(null, null, "%", null);
  			
  			//Creating a list of table names
  			while (rsAllRawTblNames.next())
  			{
  				allRawTblList.add(rsAllRawTblNames.getString("TABLE_NAME"));
  			}
  		}
  		catch (SQLException allRawTblSQLExcpn)
  		{
  			dbmd = null;
  			rsAllRawTblNames = null;
  			log.error("SQLException occurred while fetching all raw table names from database... " + allRawTblSQLExcpn.getMessage());
  		}
  		
  		disconnect(dbConnection);
  		
  		return allRawTblList;
  	}
  	
  	/**
  	 * Checks if a table exists in the database.
  	 * @param	tableName	Name of the table whose existence needs to be verified
  	 * @return	true, if the table exists in the database
  	 * 			false, if the table does not exist in the database or database connection fails
  	 */
  	public boolean tableExists(String tableName)
  	{
  		List<String> allRawTblList = getAllRawTableNames();
  		
  		if (allRawTblList != null)
  		{
  			if (allRawTblList.contains(tableName))
  	  		{
  	  			return true;
  	  		}
  		}
  		
  		return false;
  	}
  	
  	/**
  	 * Fetches all the data stored in a table in the database.
  	 * @param	tableName	Name of the table whose data needs to be fetched
  	 * @return	All the data stored in the table.
  	 * 			null, if database connection fails
  	 */
  	public List<List<Object>> getTableData(String tableName)
  	{
  		//Connecting to data base
  		Connection dbConnection = connect();
  		
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
  		String tblDataQuery = "SELECT * FROM " + tableName + ";";
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
  		}
  		catch (SQLException allTblDataSQLExcpn)
  		{
  			stmtTblData = null;
  			rsTblData = null;
  			rsmdTblData = null;
  			tblData = null;
  			log.error("SQLException occurred while fetching all the data of table " + tableName + "... " + allTblDataSQLExcpn.getMessage());
  		}
  		
  		disconnect(dbConnection);
  		
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
  		//Connecting to data base
  		Connection dbConnection = connect();
  		
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
  		
  		//Executing SQL query to get all the data of the table
  		String tblDataQuery = "SELECT * FROM " + tableName + ";";
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
  		}
  		catch (SQLException tblFieldsSQLExcpn)
  		{
  			stmtTblFields = null;
  			rsTblFields = null;
  			rsmdTblFields = null;
  			tblFields = null;
  			log.error("SQLException occurred while fetching field details of table " + tableName + "... " + tblFieldsSQLExcpn.getMessage());
  		}
  		
  		disconnect(dbConnection);
  		
  		return tblFields;
  	}
  	
  	/**
  	 * Fetches pivot table data from the database according to the input schema.
  	 * @param	pvtTblSchema	Schema defined for pivot table
  	 * @return	Pivot table data
  	 * 			null, if database connection fails
  	 */
  	public List<List<Object>> getPivotTableData(PivotTableSchema pvtTblSchema)
  	{
  		//Connecting to data base
  		Connection dbConnection = connect();
  		
  		if (dbConnection == null)							//failed connection
  		{
  			return null;
  		}
  		
  		//Proceeding, if database connection is successful  		
  		Statement stmtPvtTblData = null;
  		ResultSet rsPvtTblData = null;
  		ResultSetMetaData rsmdPvtTblData = null;
  		int fieldCount = 0;
  		List<List<Object>> pvtTblData = new ArrayList<List<Object>>();
  		List<String> rowLabels = pvtTblSchema.getRowLabels();
  		List<String> colLabels = pvtTblSchema.getColumnLabels();
  		String rowLabel = rowLabels.get(0);					//getting first row label as we have just 1 row label as of now
  		String colLabel = colLabels.get(0);					//getting first column label as we have just 1 column label as of now
  		String function = pvtTblSchema.getFunctionName();
  		String valField = pvtTblSchema.getValueField();
  		String tableName = pvtTblSchema.getTableName();
  		
  		//Executing SQL query to get pivot table data
  		String pvtTblDataQuery = "SELECT " + rowLabel + ", " 
  										   + colLabel + ", "
  										   + function + "("
  										   + valField + ")"
  									 + " FROM " + tableName
  									 + " GROUP BY " + rowLabel + ", "
  									 				+ colLabel + ";";
  		try
  		{
  			stmtPvtTblData = dbConnection.createStatement();
  			log.info("Running query " + pvtTblDataQuery);
  			rsPvtTblData = stmtPvtTblData.executeQuery(pvtTblDataQuery);
  			
  			//Fetching field count for the results returned by the SQL query executed
  			rsmdPvtTblData = rsPvtTblData.getMetaData();
  			fieldCount = rsmdPvtTblData.getColumnCount();
  			
  			while (rsPvtTblData.next())
  			{
  				List<Object> tblRecord = new ArrayList<Object>();
  				
  				for (int i=1; i<=fieldCount; i++)
  				{
  					tblRecord.add(rsPvtTblData.getObject(i));
  				}
  				
  				pvtTblData.add(tblRecord);
  			}
  		}
  		catch (SQLException pvtTblDataSQLExcpn)
  		{
  			stmtPvtTblData = null;
  			rsPvtTblData = null;
  			rsmdPvtTblData = null;
  			pvtTblData = null;
  			log.error("SQLException occurred while fetching pivot table data... " + pvtTblDataSQLExcpn.getMessage());
  		}
  		
  		disconnect(dbConnection);
  		
  		return pvtTblData;
  	}
}
