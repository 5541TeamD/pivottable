package ca.concordia.pivottable.servicelayer.impl;

import ca.concordia.pivottable.datalayer.DataSourceAccess;
import ca.concordia.pivottable.servicelayer.DataRetrievalService;
import java.util.ArrayList;
import java.util.List;
import ca.concordia.pivottable.entities.DataSet;
import ca.concordia.pivottable.entities.DataType;
import ca.concordia.pivottable.entities.DataField;
import ca.concordia.pivottable.entities.PivotTable;
import ca.concordia.pivottable.entities.PivotTableSchema;

/**
 * Handles retrieval of data from the data source and its supply to the Controller.
 * Data retrieved is converted into Entity objects before being sent to the Controller.
 * @author Annabelle Williams
 * @author Jyotsana Gupta
 */
public class DataRetrievalServiceImpl implements DataRetrievalService {
	/**
	 * Data source object used for performing data retrieval operations.
	 */
	private DataSourceAccess dataSource;

	/**
	 * Class constructor.
	 */
	public DataRetrievalServiceImpl(DataSourceAccess dataSource)
	{
		this.dataSource = dataSource;
	}

	/**
	 * Tests the connection to the data source by first connecting and then disconnecting.
	 *
	 * @return true, if connection test is successful
	 * false, if connection test fails
	 */
	public boolean checkDataSourceConnection() {
		return dataSource.testConnection();
	}

	/**
	 * Fetches the names of all available raw reports from the data source.
	 *
	 * @return List of all available raw report names
	 * null, if data source connection fails
	 */
	public List<String> getAllRawReportNames() {
		return dataSource.getAllRawTableNames();
	}

	/**
	 * Checks if a raw report exists in the data source.
	 *
	 * @param    reportName    Name of the raw report whose existence needs to be verified
	 * @return true, if the report exists in the data source
	 * false, if the report does not exist in the data source or data source connection fails
	 */
	public boolean rawReportExists(String reportName) {
		return dataSource.tableExists(reportName);
	}

	/**
	 * Fetches all the data and field details from a raw report stored in the data source.
	 *
	 * @param    reportName    Name of the raw report whose information needs to be fetched
	 * @return All the information stored in the raw report.
	 * null, if data source connection fails
	 */
	public DataSet getRawReport(String reportName) {
		List<String[]> dataFields = dataSource.getTableFields(reportName);
		List<DataField> rawDataFields = new ArrayList<DataField>();
		List<List<Object>> rawReportData = new ArrayList<List<Object>>();

		for (String[] dataField : dataFields) {
			DataType rawFieldType = DataType.getDataType(dataField[1]);
			DataField rawDataField = new DataField(dataField[0], rawFieldType);
			rawDataFields.add(rawDataField);
		}

		rawReportData = dataSource.getTableData(reportName);
		DataSet rawReport = new DataSet(rawDataFields, rawReportData);

		return rawReport;
	}

	/**
	 * Fetches pivot table data from the data source according to the input schema.
	 *
	 * @param    pvtTblSchema    Schema defined for pivot table
	 * @return Pivot table, containing the data and the schema
	 * null, if database connection fails
	 */
	public PivotTable getPivotTable(PivotTableSchema pvtTblSchema)
	{
		PivotTable pivotTable = new PivotTable(pvtTblSchema, dataSource.getPivotTableData(pvtTblSchema));

		return pivotTable;
	}
}
