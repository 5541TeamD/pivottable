package ca.concordia.pivottable.datalayer.impl;

import ca.concordia.pivottable.datalayer.DataSourceAccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// TODO remove this. It is just an example
public class DataSourceAccessImpl implements DataSourceAccess {
    //Setting database connection variables
    final static String jdbcDriver = "com.mysql.jdbc.Driver";				//JDBC Driver Name
    final static String dbName = "testdb";							//Database Name
    final static String dbUrl = "jdbc:mysql://localhost:3306/" + dbName;			//Database URL
    final static String dbUsername = "warmup";						//Database Username
    final static String dbPassword = "warmup";						//Database Password

    Connection conn;

    public DataSourceAccessImpl() {

    }

    public void connect() {
        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException cne) {
            System.err.println("JDBC driver not found...");
        }
    }

    public void disconnect() {
        try {
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            // nothing we can do about it
        }
    }

    public List<String> getAllTableNames() {
        //Fetching all available table names from the database
        List<String> list = new ArrayList<>();
        if (conn == null) {
            return list;
        }
        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rsAllTables = dbmd.getTables(null, null, "%", null);
            while (rsAllTables.next()) {
                list.add(rsAllTables.getString("TABLE_NAME"));
            }
            rsAllTables.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return list;
    }
}
