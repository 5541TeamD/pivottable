package ca.concordia.pivottable.datalayer.impl;

import ca.concordia.pivottable.datalayer.DataSourceAccess;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
//TODO
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import java.util.Set;

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
	 * Used for logging information, warning and error messages during application run.
	 */
	//TODO
	//private Logger log = LoggerFactory.getLogger(DataSourceAccessImpl.class);
	
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
			//TODO
			//log.error("Unexpected exception occurred while attempting DB connection... " + dbConnGenExcpn.getMessage());
		}		
		
		//TODO
		//log.info("Initiating connection to database " + dbUrl + "...");
		
		try
		{
			dbConnection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		}
		catch (SQLException dbConnSQLExcpn)
		{
			dbConnection = null;
			//TODO
			//log.error("SQLException occurred while connecting to database... " + dbConnSQLExcpn.getMessage());
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
    			//TODO
    			//log.error("SQLException occurred while disconnecting from database... " + dbDisconnSQLExcpn.getMessage());
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
  				Statement stmt = dbConnection.createStatement();
  				rsAllRawTblNames = stmt.executeQuery(allRawTblNamesQuery);
  				stmt.close();
  			}
  			  			
  			//Creating a list of table names
  			while (rsAllRawTblNames.next())
  			{
  				allRawTblList.add(rsAllRawTblNames.getString("TABLE_NAME"));
  			}
  			
  			rsAllRawTblNames.close();
  		}
  		catch (SQLException allRawTblSQLExcpn)
  		{
  			rsAllRawTblNames = null;
  			//TODO
  			//log.error("SQLException occurred while fetching all raw table names from database... " + allRawTblSQLExcpn.getMessage());
  		}
  		finally
  		{
  			disconnect();
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
  			//TODO
  			//log.info("Running query " + tblDataQuery);
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
  			//TODO
  			//log.error("SQLException occurred while fetching all the data of table " + tableName + "... " + allTblDataSQLExcpn.getMessage());
  		}
  		finally
  		{
  			disconnect();
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
  			//TODO
  			//log.info("Running query " + tblDataQuery);
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
  			//TODO
  			//log.error("SQLException occurred while fetching field details of table " + tableName + "... " + tblFieldsSQLExcpn.getMessage());
  		}
  		finally
  		{
  			disconnect();
  		}
  		
  		return tblFields;
  	}
  	
  	/**
  	 * Executes SQL query on database and fetches pivot table data.
  	 * @param	rowLabels	List of row labels selected as part of pivot table schema
  	 * @param	colLabels	List of column labels selected as part of pivot table schema
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
  													String filterField, String filterValue, String sortField, String sortOrder, String tableName)
  	{
  		if (dbConnection == null)							//failed connection
  		{
  			return null;
  		}
  		
  		//Proceeding, if database connection is successful
  		String sortClause = " ";
  		String filterClause = " ";
  		String selectClause = " SELECT ";
  		String grpClause = " GROUP BY ";
  		List<List<List<Object>>> pvtTblData = new ArrayList<List<List<Object>>>();
  		
  		//Generating the SQL query select clause for selecting row labels
  		for (String rowLabel : rowLabels)
  		{
  			selectClause = selectClause + rowLabel + ", ";
  		}
  		
  		//Generating the SQL query select clause for selecting column labels
  		for (String colLabel : colLabels)
  		{
  			selectClause = selectClause + colLabel + ", ";
  		}
  		
  		//Generating the SQL query group by clause for grouping by row labels
  		for (String rowLabel : rowLabels)
  		{
  			grpClause = grpClause + rowLabel + ", ";
  		}
  		
  		//Generating the SQL query group by clause for grouping by column labels
  		for (String colLabel : colLabels)
  		{
  			grpClause = grpClause + colLabel + ", ";
  		}
  		
  		//Removing trailing comma if no there is no page label
  		if (pageLabel == null)
  			grpClause = grpClause.substring(0, grpClause.lastIndexOf(","));
  		
  		//Generating the SQL query clause for filtering resulting data
  		if ((filterField != null && !filterField.trim().isEmpty()) && (filterValue != null))
  			filterClause = " WHERE " + filterField + " = \'" + filterValue + "\'";
  		
  		//Generating the SQL query clause for sorting resulting data
  		if ((sortField != null && !sortField.trim().isEmpty()) && (sortOrder != null))
  			sortClause = " ORDER BY " + sortField + " " + sortOrder;
  		
  		//Generating and executing the SQL query
  		if (pageLabel == null)
  			pvtTblData = executeQuery(dbConnection, selectClause, function, valField, tableName, filterClause, sortClause);
  		else
  			pvtTblData = executeQuery(dbConnection, selectClause, pageLabel, function, valField, tableName, filterClause, sortClause);
  		
  		disconnect();
  		
  		return pvtTblData;
  	}
  	
  	/**
  	 * Generates and executes an SQL query (without page labels).
  	 * @param 	dbConnection	An object of type Connection referring to the data source connection used for executing the query
  	 * @param 	selectClause	SQL query clause used for selecting row and column labels values as per the schema
  	 * @param 	function		Mathematical function selected as part of pivot table schema
  	 * @param 	valField		Value field selected as part of pivot table schema
  	 * @param 	tableName		Raw report table name
  	 * @param 	filterClause	SQL query clause used for filtering pivot table data as per the schema
  	 * @param 	sortClause		SQL query clause used for sorting pivot table data as per the schema
  	 * @return	Pivot table data fetched from the database
  	 */
  	private List<List<List<Object>>> executeQuery(Connection dbConnection, String selectClause, String function, String valField, String tableName, String filterClause, String sortClause) 
  	{
  		String pvtTblDataQuery = null;  		
  		Statement stmtPvtTblData = null;
  		ResultSet rsPvtTblData = null;
  		ResultSetMetaData rsmdPvtTblData = null;
  		int fieldCount = 0;
  		Set<List<Object>> rowColList = new HashSet<List<Object>>();
  		List<List<Object>> pageData = new ArrayList<List<Object>>();
  		List<List<List<Object>>> pvtTblData = new ArrayList<List<List<Object>>>();
  		
  		//Generating the SQL query to get pivot table data without page label
  		pvtTblDataQuery = selectClause
  							+ valField + " "
  							+ " FROM ( SELECT * FROM " + tableName
  										+ filterClause
										+ " LIMIT " + String.valueOf(ROW_LIMIT) + " ) as sublist"
							+ sortClause + ";";
  		
  		//Executing the SQL query
  		try
  		{
  			stmtPvtTblData = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
  			//TODO
  			//log.info("Running query " + pvtTblDataQuery);
  			rsPvtTblData = stmtPvtTblData.executeQuery(pvtTblDataQuery);
  			
  			//Fetching field count for the results returned by the SQL query executed
  			rsmdPvtTblData = rsPvtTblData.getMetaData();
  			fieldCount = rsmdPvtTblData.getColumnCount();
  			
  			//Fetching pivot table row and column label values
  			while (rsPvtTblData.next())
  			{
  				List<Object> recordRowCol = new ArrayList<Object>();
  				
  				for (int i=1; i<fieldCount; i++)
  				{
  					recordRowCol.add(rsPvtTblData.getObject(i));
  				}
  				
  				rowColList.add(recordRowCol);
  			}
  			
  			//Calculating function field values
  			int i = 0;
  			for (List<Object> recordRowCol : rowColList)
  			{
  				double result = 0;
  				List<Integer> valueList = new ArrayList<Integer>();
  				List<String> countValueList = new ArrayList<String>();
  			
  				rsPvtTblData.beforeFirst();
  				while (rsPvtTblData.next())
  				{
  					boolean getValue = true;
  					for (i=1; i<fieldCount; i++)
  	  				{
  	  					if (!rsPvtTblData.getObject(i).equals(recordRowCol.get(i-1)))
  	  					{
  	  						getValue = false;
  	  						break;
  	  					}
  	  				}
  					
  					if (getValue)
  					{
  						if (function.equalsIgnoreCase("count"))
							countValueList.add(rsPvtTblData.getObject(i).toString());
						else
						{
							int currValue = (Integer)rsPvtTblData.getObject(i);
							valueList.add(currValue);
						}
  					}
  				}
  				
  				if (function.equalsIgnoreCase("count"))
  					result = countValueList.size();
  				else
  					result = calcFunctionValue(function, valueList);
  				recordRowCol.add(fieldCount-1, result);
  				pageData.add(recordRowCol);
  			}
  			
  			//Storing entire pivot table data as the first page since there is only one page in this case
  			pvtTblData.add(pageData);
  			
  			rsPvtTblData.close();
  			stmtPvtTblData.close();
  		}
  		catch (SQLException pvtTblDataSQLExcpn)
  		{
  			stmtPvtTblData = null;
  			rsPvtTblData = null;
  			rsmdPvtTblData = null;
  			pvtTblData = null;
  			//TODO
  			//log.error("SQLException occurred while fetching pivot table data... " + pvtTblDataSQLExcpn.getMessage());
  		}
  		
  		return pvtTblData;
  	}
  	
  	/**
  	 * Generates and executes an SQL query (with page labels).
  	 * @param 	dbConnection	An object of type Connection referring to the data source connection used for executing the query
  	 * @param 	selectClause	SQL query clause used for selecting row and column labels values as per the schema
  	 * @param	pageLabel		Page label selected as part of pivot table schema
  	 * @param 	function		Mathematical function selected as part of pivot table schema
  	 * @param 	valField		Value field selected as part of pivot table schema
  	 * @param 	tableName		Raw report table name
  	 * @param 	filterClause	SQL query clause used for filtering pivot table data as per the schema
  	 * @param 	sortClause		SQL query clause used for sorting pivot table data as per the schema
  	 * @return	Pivot table data fetched from the database
  	 */
  	private List<List<List<Object>>> executeQuery(Connection dbConnection, String selectClause, String pageLabel, String function, String valField, String tableName, String filterClause, String sortClause) 
  	{
  		String pvtTblDataQuery = null;  		
  		Statement stmtPvtTblData = null;
  		ResultSet rsPvtTblData = null;
  		ResultSetMetaData rsmdPvtTblData = null;
  		int fieldCount = 0;
  		List<String> pageLabelValues = new ArrayList<String>();
  		List<List<List<Object>>> pvtTblData = new ArrayList<List<List<Object>>>();
  		
  		//Fetching all the values of the selected page label column
  		pageLabelValues = getPageLabelValues(pageLabel, tableName);
  		
  		if (pageLabelValues != null) 
  		{
			//Fetching pivot table data per page
	  		for (String pageValue : pageLabelValues) 
	  		{
	  			//Generating the page label selection clause
	  	  		String pageLabelClause;
	  	  		if (filterClause.trim().isEmpty())
	  	  			pageLabelClause = " WHERE " + pageLabel + " = \'" + pageValue + "\' ";
	  	  		else
	  	  			pageLabelClause = " AND " + pageLabel + " = \'" + pageValue + "\' ";
	  			
	  			//Generating the SQL query to get pivot table data for one page label value
	  	  		pvtTblDataQuery = selectClause
	  	  							+ valField + " "
	  	  							+ " FROM ( SELECT * FROM " + tableName
	  	  										+ filterClause
	  	  										+ pageLabelClause
	  											+ " LIMIT " + String.valueOf(ROW_LIMIT) + " ) as sublist"
	  								+ sortClause + ";";
	  	  		
	  	  		//Executing the SQL query
	  	  		try
	  	  		{
	  	  			stmtPvtTblData = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	  	  			//TODO
	  	  			//log.info("Running query " + pvtTblDataQuery);
	  	  			rsPvtTblData = stmtPvtTblData.executeQuery(pvtTblDataQuery);
	  	  			
	  	  			//Fetching field count for the results returned by the SQL query executed
	  	  			rsmdPvtTblData = rsPvtTblData.getMetaData();
	  	  			fieldCount = rsmdPvtTblData.getColumnCount();
	  	  			
	  	  			//Fetching pivot table row and column label values for one page
	  	  			List<List<Object>> pageData = new ArrayList<List<Object>>();
	  	  			Set<List<Object>> rowColList = new HashSet<List<Object>>();
	  	  		
	  	  			while (rsPvtTblData.next())
	  	  			{
	  	  				List<Object> recordRowCol = new ArrayList<Object>();
	  	  				
	  	  				for (int i=1; i<fieldCount; i++)
	  	  				{
	  	  					recordRowCol.add(rsPvtTblData.getObject(i));
	  	  				}
	  	  				
	  	  				rowColList.add(recordRowCol);
	  	  			}
	  	  			
	  	  			//Calculating function field values
	  	  			int i = 0;
	  	  			for (List<Object> recordRowCol : rowColList)
	  	  			{
	  	  				double result = 0;
	  	  				List<Integer> valueList = new ArrayList<Integer>();
	  	  				List<String> countValueList = new ArrayList<String>();
	  	  			
	  	  				rsPvtTblData.beforeFirst();
	  	  				while (rsPvtTblData.next())
	  	  				{
	  	  					boolean getValue = true;
	  	  					for (i=1; i<fieldCount; i++)
	  	  	  				{
	  	  	  					if (!rsPvtTblData.getObject(i).equals(recordRowCol.get(i-1)))
	  	  	  					{
	  	  	  						getValue = false;
	  	  	  						break;
	  	  	  					}
	  	  	  				}
	  	  					
	  	  					if (getValue)
	  	  					{
	  	  						if (function.equalsIgnoreCase("count"))
	  	  							countValueList.add(rsPvtTblData.getObject(i).toString());
	  	  						else
	  	  						{
		  	  						int currValue = (Integer)rsPvtTblData.getObject(i);
		  	  						valueList.add(currValue);
	  	  						}
	  	  					}
	  	  				}
	  	  				
	  	  				if (function.equalsIgnoreCase("count"))
							result = countValueList.size();
						else
							result = calcFunctionValue(function, valueList);
	  	  				
	  	  				recordRowCol.add(fieldCount-1, result);
	  	  				pageData.add(recordRowCol);
	  	  			}
	  	  			
	  	  			//Adding data for this particular page to the complete pivot table data set
	  	  			pvtTblData.add(pageData);
	  	  			
	  	  			rsPvtTblData.close();
	  	  			stmtPvtTblData.close();
	  	  		}
	  	  		catch (SQLException pvtTblDataSQLExcpn)
	  	  		{
	  	  			stmtPvtTblData = null;
	  	  			rsPvtTblData = null;
	  	  			rsmdPvtTblData = null;
	  	  			pvtTblData = null;
	  	  			//TODO
	  	  			//log.error("SQLException occurred while fetching pivot table data... " + pvtTblDataSQLExcpn.getMessage());
	  	  		}
	  		}
  		}
	  
  		return pvtTblData;
  	}
  	
  	/**
  	 * Calculates different summary function values.
  	 * @param 	functionName		Name of the function to be calculated
  	 * @param 	valueList			List of values to be used in calculation
  	 * @return	The result calculated
  	 */  	  	
  	private double calcFunctionValue(String functionName, List<Integer> valueList)
  	{
  		double result = 0;
  		
  		if (functionName.equalsIgnoreCase("Sum"))
  		{
  			result = 0;
  			for (double value : valueList)
  				result += value;
  		}
  		else if (functionName.equalsIgnoreCase("Min"))
  		{
  			result = valueList.get(0);
  			for (double value : valueList)
  			{
  				if (value < result)
  					result = value;
  			}
  		}
  		else if (functionName.equalsIgnoreCase("Max"))
  		{
  			result = valueList.get(0);
  			for (double value : valueList)
  			{
  				if (value > result)
  					result = value;
  			}
  		}
  		else if (functionName.equalsIgnoreCase("Avg"))
  		{
  			result = 0;
  			for (double value : valueList)
  				result += value;
  			result = (result / (valueList.size()));
  		}
  		else if (functionName.equalsIgnoreCase("Product"))
  		{
  			result = 1;
  			for (double value : valueList)
  				result *= value;
  		}
  		else if (functionName.equalsIgnoreCase("Variance"))
  		{
  			double sum = 0;
  			for (double value : valueList)
  				sum += value; 
  			
  			double avg = (sum/valueList.size());
  			
  			double sumSqrDiff = 0;  			
  			for (double value : valueList)
  			{
  				double sqrDiff = (Math.pow((value - avg), 2));
  				sumSqrDiff += sqrDiff; 
  			}
  			
  			result = (sumSqrDiff/valueList.size());
  		}
  		else if (functionName.equalsIgnoreCase("Standard Deviation"))
  		{
  			double sum = 0;
  			for (double value : valueList)
  				sum += value; 
  			
  			double avg = (sum/valueList.size());
  			
  			double sumSqrDiff = 0;  			
  			for (double value : valueList)
  			{
  				double sqrDiff = (Math.pow((value - avg), 2));
  				sumSqrDiff += sqrDiff; 
  			}
  			
  			result = (Math.sqrt((sumSqrDiff/valueList.size())));
  		}
  		
  		return result;
  	}
  	
  	/**
  	 * Executes SQL query on the database and fetches all the values of the selected page label column 
  	 * @param	pageLabel		Page label column selected as part of pivot table schema
  	 * @param	tableName		Raw report table name
  	 * @return	List of page label column values
  	 */
  	public List<String> getPageLabelValues(String pageLabel, String tableName)
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
  		
  		//Generating the SQL query to get all the values of the selected page label column
		pageLabelQuery = "SELECT DISTINCT " + pageLabel
							+ " FROM " + tableName + ";";
		
		//Executing the page label SQL query
  		try
  		{
  			stmtPageLabels = dbConnection.createStatement();
  			//TODO
  			//log.info("Running query " + pageLabelQuery);
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
  			//TODO
  			//log.error("SQLException occurred while fetching page label values... " + pageLabelsSQLExcpn.getMessage());
  		}
  		finally
  		{
  			disconnect();
  		}
  		
  		return pageLabelValues;
  	}
}
