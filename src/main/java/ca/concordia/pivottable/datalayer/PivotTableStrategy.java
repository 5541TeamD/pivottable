package ca.concordia.pivottable.datalayer;

import java.sql.Connection;
import java.util.List;

/**
 * Interface to be implemented by different strategies used for retrieving pivot table data from the database.
 * @author	Jyotsana Gupta
 * @version	1.0
 */
public interface PivotTableStrategy 
{
	/**
	 * Executes a specific strategy for retrieving pivot table data.
	 * @param	dbConnection	Database connection object
	 * @param	rowLimit		Maximum number of rows to be fetched by any SQL query
	 * @return	Pivot table data
	 */
	List<List<List<Object>>> getPvtTblData(Connection dbConnection, int rowLimit);
}
