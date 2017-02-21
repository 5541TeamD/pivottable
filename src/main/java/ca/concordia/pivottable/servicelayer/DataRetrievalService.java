package ca.concordia.pivottable.servicelayer;

import java.util.List;
import ca.concordia.pivottable.entities.DataSet;
import ca.concordia.pivottable.entities.PivotTableSchema;
import ca.concordia.pivottable.entities.PivotTable;

/**
 * Defines the interface for data retrieval service operations.
 * @author Jyotsana Gupta
 * @version 1.0
 */
public interface DataRetrievalService 
{    
    /**
     * Tests the connection to the data source by first connecting and then disconnecting.
     * @return	true, if connection test is successful
     * 			false, if connection test fails
     */
	boolean checkDataSourceConnection();
	
	/**
     * Fetches the names of all available raw reports from the data source. 
     * @return	List of all available raw report names
     * 			null, if data source connection fails
     */
	List<String> getAllRawReportNames();
	
	/**
  	 * Checks if a raw report exists in the data source.
  	 * @param	reportName	Name of the raw report whose existence needs to be verified
  	 * @return	true, if the report exists in the data source
  	 * 			false, if the report does not exist in the data source or data source connection fails
  	 */
	boolean rawReportExists(String reportName);
	
	/**
  	 * Fetches all the data and field details from a raw report stored in the data source.
  	 * @param	reportName	Name of the raw report whose information needs to be fetched
  	 * @return	All the information stored in the raw report.
  	 * 			null, if data source connection fails
  	 */
	DataSet getRawReport(String reportName);
	
	/**
  	 * Fetches pivot table data from the data source according to the input schema.
  	 * @param	pvtTblSchema	Schema defined for pivot table
  	 * @return	Pivot table, containing the data and the schema
  	 * 			null, if database connection fails
  	 */
	PivotTable getPivotTable(PivotTableSchema pvtTblSchema);
}
