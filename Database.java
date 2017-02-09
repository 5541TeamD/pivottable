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
	private static Connection dbConnection = null;
	
	//Defining accessor methods
	public String getJDBCDriver()
	{
		return this.jdbcDriver;
	}
	
	public String getDBName()
	{
		return this.dbName;
	}
	
	public String getDBUrl()
	{
		return this.dbUrl;
	}
	
	public String getDBUsername()
	{
		return this.dbUsername;
	}
	
	public String getDBPassword()
	{
		return this.dbPassword;
	}
	
	//Defining mutator methods
	public void setJDBCDriver(String jdbcDriver)
	{
		this.jdbcDriver = jdbcDriver;
	}
	
	public void setDBName(String dbName)
	{
		this.dbName = dbName;
	}
	
	public void setDBUrl(String dbUrl)
	{
		this.dbUrl = dbUrl;
	}
	
	public void setDBUsername(String dbUsername)
	{
		this.dbUsername = dbUsername;
	}
	
	public void setDBPassword(String dbPassword)
	{
		this.dbPassword = dbPassword;
	}
	
	//Method for connecting to DB
	public boolean connectToDB()
	{
		//Setting database connection variables
		setJDBCDriver("com.mysql.jdbc.Driver");
		setDBName("testdb");
		setDBUrl("jdbc:mysql://localhost:3306/" + getDBName());
		setDBUsername("root");
		setDBPassword("root");
		
		try
		{
			//Loading JDBC Driver class at run-time
			Class.forName(getJDBCDriver());
		}
		catch (Exception genericException)
		{
			System.out.println();
			System.out.println("Unexpected exception occurred...");
			genericException.printStackTrace();
		}
		
		System.out.println("Initiating connection to database " + getDBName() + "...");
		
		try
		{
			dbConnection = DriverManager.getConnection(getDBUrl(), getDBUsername(), getDBPassword());
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