package teamd.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.ArrayList;

public class Database 
{
	//Database attributes required for connection
	private String jdbcDriver = new String();
	private String dbName = new String();
	private String dbUrl = new String();
	private String dbUsername = new String();
	private String dbPassword = new String();
	
	//Global database connection state
	private Connection dbConnection = null;
	
	//Method for connecting to DB
	public boolean connectToDB(String username, String password)
	{
		//Setting database connection variables
		jdbcDriver = "com.mysql.jdbc.Driver";
		dbName = "testdb";
		dbUrl = "jdbc:mysql://localhost:3306/" + dbName;
		dbUsername = username;
		dbPassword = password;
		
		try
		{
			//Loading JDBC Driver class at run-time
			Class.forName(jdbcDriver);
		}
		catch (Exception genericException)
		{
			System.out.println();
			System.out.println("Unexpected exception occurred...");
			genericException.printStackTrace();
		}
		
		System.out.println("Initiating connection to database " + dbName + "...");
		
		try
		{
			dbConnection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		}
		catch (SQLException dbConnSQLExp)
		{
			dbConnection = null;
			System.out.println();
			System.out.println("SQLException occurred while connecting to database...");
			dbConnSQLExp.printStackTrace();
		}
		
		if (dbConnection == null)
			return true;
		else
			return false;
	}
	
	//Method for fetching all table names from database
	public List<String> getAllTables()
	{
		DatabaseMetaData dbmd = null;
		ResultSet rsAllTables = null;
		List<String> allTableList = new ArrayList<String>();
		
		try
		{
			dbmd = dbConnection.getMetaData();
			rsAllTables = dbmd.getTables(null, null, "%", null);
			
			while (rsAllTables.next())
			{
				allTableList.add(rsAllTables.getString("TABLE_NAME"));
			}
		}
		catch (SQLException getAllTblSQLExp)
		{
			dbmd = null;
			rsAllTables = null;
			System.out.println();
			System.out.println("SQLException occurred while fetching all table names from database...");
			getAllTblSQLExp.printStackTrace();
		}
		
		return allTableList;
	}
	
	//Method for checking if table exists in database
	public boolean tableExists(String tableName)
	{
		List<String> allTableList = getAllTables();
		
		for (String listTableName: allTableList)
			if (listTableName.trim().contains(tableName))
				return true;
		
		return false;
	}
	
	//Method for fetching table data
	public List<List<Object>> getTableData (String tableName)
	{
		Statement stmt = null;
		ResultSet rsAllTableData = null;
		ResultSetMetaData rsmdAllTableData = null;
		int columnCount = 0;
		List<List<Object>> allTableData = new ArrayList<List<Object>>();
		
		//Executing SQL query to get complete data of a table
		String allTableDataQuery = "SELECT * FROM " + tableName + ";";
		try
		{
			stmt = dbConnection.createStatement();
			rsAllTableData = stmt.executeQuery(allTableDataQuery);
			
			//Fetching column count returned by the SQL query executed
			rsmdAllTableData = rsAllTableData.getMetaData();
			columnCount = rsmdAllTableData.getColumnCount();
			
			while (rsAllTableData.next())
			{
				List<Object> tableRecord = new ArrayList<Object>();
				
				for (int i=1; i<=columnCount; i++)
				{
					tableRecord.add(rsAllTableData.getObject(i));
				}
				
				allTableData.add(tableRecord);
			}
		}
		catch (SQLException getAllDataSQLExp)
		{
			stmt = null;
			rsAllTableData = null;
			System.out.println();
			System.out.println("SQLException occurred while fetching complete data of table " + tableName + "...");
			getAllDataSQLExp.printStackTrace();
		}
		
		return allTableData;
	}
}