package ca.concordia.pivottable.servicelayer.impl;

import ca.concordia.pivottable.datalayer.DataSourceAccess;
import ca.concordia.pivottable.servicelayer.CredentialsService;
import ca.concordia.pivottable.servicelayer.DataRetrievalService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	 * Credentials service object used for setting the data source connection credentials received from the view.
	 */
	private CredentialsService credentials;

	/**
	 * Class constructor.
	 */
	public DataRetrievalServiceImpl(DataSourceAccess dataSource, CredentialsService credentialsService)
	{
		this.credentials = credentialsService;
		this.dataSource = dataSource;
		dataSource.setCredentials(credentials.getDataSource(), credentials.getUsername(), credentials.getPassword());		
	}

	/**
	 * Tests the connection to the data source by first connecting and then disconnecting.
	 * @return true, if connection test is successful
	 * false, if connection test fails
	 */
	public boolean checkDataSourceConnection() {
		return dataSource.testConnection();
	}

	/**
	 * Fetches the names of all available raw reports from the data source.
	 * @return List of all available raw report names
	 * null, if data source connection fails
	 */
	public List<String> getAllRawReportNames() {
		return dataSource.getAllRawTableNames();
	}

	/**
	 * Checks if a raw report exists in the data source.
	 * @param    reportName    Name of the raw report whose existence needs to be verified
	 * @return true, if the report exists in the data source
	 * false, if the report does not exist in the data source or data source connection fails
	 */
	public boolean rawReportExists(String reportName) {
		return dataSource.tableExists(reportName);
	}

	/**
	 * Fetches all the data and field details from a raw report stored in the data source.
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
	 * @param	pvtTblSchema	Schema defined for pivot table
	 * @return	Pivot table, containing the data and the schema
	 * 			null, if database connection fails
	 */
	public PivotTable getPivotTable(PivotTableSchema pvtTblSchema)
	{
		List<String> rowLabels = pvtTblSchema.getRowLabels();
  		List<String> colLabels = pvtTblSchema.getColumnLabels();
  		String pageLabel = pvtTblSchema.getPageLabel();
  		String function = pvtTblSchema.getFunctionName();
  		String valField = pvtTblSchema.getValueField();
  		String filterField = pvtTblSchema.getFilterField();
  		String filterValue = pvtTblSchema.getFilterValue();
  		String sortField = pvtTblSchema.getSortField();
  		String sortOrder = pvtTblSchema.getSortOrder();
  		String tableSummFuncName = pvtTblSchema.getTableSummFuncName();
  		String tableName = pvtTblSchema.getTableName();
  		List<String> pageLabelValues = new ArrayList<String>();
  		List<List<List<Object>>> pvtTblData = new ArrayList<List<List<Object>>>();
  		
  		if (pageLabel == null || pageLabel.trim().equals(""))
  		{
  			pageLabelValues = new ArrayList<>(); // empty page labels
  			
	  		//Executing the SQL query to get pivot table data without page label
  			pvtTblData = dataSource.getPvtTblData(rowLabels, colLabels, function, valField, filterField, filterValue, sortField, sortOrder, tableName);
  		}
  		else
  		{
  			//Fetching all the values of the selected page label column
  			pageLabelValues = dataSource.getPageLabelValues(pageLabel, tableName);
  			
  			//Executing the SQL query to get pivot table data with page label
  			pvtTblData = dataSource.getPvtTblData(rowLabels, colLabels, pageLabel, function, valField, filterField, filterValue, sortField, sortOrder, tableName);
  		}
  		
  		//Fetching pivot table row, column, page and table level summary details
  		List<List<List<List<Object>>>> oneDimSummaryDetails = getDimSummaryDetails(pvtTblData, tableSummFuncName, rowLabels.size(), colLabels.size());
  		List<List<List<Object>>> rowSummDetails = oneDimSummaryDetails.get(0);
  		List<List<List<Object>>> colSummDetails = oneDimSummaryDetails.get(1);
  		List<Double> pageSummDetails = getPageSummary(pvtTblData, tableSummFuncName);
  		double tableSummDetails = getTableSummary(pvtTblData, tableSummFuncName);

  		//Creating Pivot Table with the fetched information
		PivotTable pivotTable = new PivotTable(pvtTblSchema, pageLabelValues, pvtTblData, rowSummDetails, colSummDetails, pageSummDetails, tableSummDetails);

		return pivotTable;
	}
	
	/**
	 * Fetches row-level and column-level summary details for the pivot table.
	 * @param 	pvtTblData			Complete pivot table data
	 * @param 	tableSummFuncName	Name of the summary function to be applied on pivot table
	 * @param 	rowLabelCount		Level of grouping for pivot table rows
	 * @param 	colLabelCount		Level of grouping for pivot table columns
	 * @return	Row and column level summary details
	 */
	private List<List<List<List<Object>>>> getDimSummaryDetails(List<List<List<Object>>> pvtTblData, String tableSummFuncName, int rowLabelCount, int colLabelCount)
	{
		List<List<List<List<Object>>>> tblRowColList = new ArrayList<List<List<List<Object>>>>();
		
		List<List<List<Object>>> tableRowList = getOneDimensionValues(pvtTblData, rowLabelCount, 0);
 		
 		List<List<List<Object>>> tableColList = getOneDimensionValues(pvtTblData, colLabelCount, rowLabelCount);
 		
 		List<List<List<Object>>> tblRowSummList = getOneDimensionSummary(tableRowList, tableSummFuncName);
 		
 		List<List<List<Object>>> tblColSummList = getOneDimensionSummary(tableColList, tableSummFuncName);
 		
 		tblRowColList.add(tblRowSummList);
 		tblRowColList.add(tblColSummList);
 		
 		return tblRowColList;
	}
	
	/**
	 * Fetches all label values and function field values for one dimension (row or column) of the pivot table.
	 * @param 	pvtTblData			Complete pivot table data
	 * @param 	labelCount			Level of grouping for the dimension (row or column)
	 * @param	counter				Counter reference for fetching row/column labels
	 * @return	All the details of all the levels of one dimension (row or column) of pivot table 
	 */
	private List<List<List<Object>>> getOneDimensionValues(List<List<List<Object>>> pvtTblData, int labelCount, int counter)
	{
		int currDimLblCount = labelCount;
		List<List<List<Object>>> tableDimList = new ArrayList<List<List<Object>>>();
		
 		for (List<List<Object>> pageData : pvtTblData)
		{
 			currDimLblCount = labelCount;
 			while (currDimLblCount > 0)
 			{
 				List<List<Object>> pageDimList = new ArrayList<List<Object>>();
 				for (List<Object> tblRecord : pageData)
 				{
 					List<Object> dimList = new ArrayList<Object>();
 					for (int i=counter; i<counter+currDimLblCount; i++)
 					{
 						dimList.add(tblRecord.get(i));
 					}
 					dimList.add(tblRecord.get(tblRecord.size() - 1));
 					pageDimList.add(dimList);
 				}
 				tableDimList.add(pageDimList);
 				
 				--currDimLblCount;
 			}
		}
 		
 		return tableDimList;
	}
	
	/**
	 * Fetches summary details for one dimension (row or column) of the pivot table.
	 * @param 	tableDimList		Complete details of the dimension
	 * @param 	tableSummFuncName	Name of the summary function to be applied on the dimension
	 * @return	Summary details of all the levels of one dimension (row or column) of pivot table
	 */
	private List<List<List<Object>>> getOneDimensionSummary(List<List<List<Object>>> tableDimList, String tableSummFuncName)
	{
		List<List<List<Object>>> tblDimSummList = new ArrayList<List<List<Object>>>();
		
 		for (List<List<Object>> pageDimList : tableDimList)
 		{
 			Set<List<Object>> pageDimSet = new HashSet<List<Object>>();
 			for (List<Object> dimRecord : pageDimList)
 			{
 				List<Object> dimLblList = new ArrayList<Object>();
 				for (int i=0; i<dimRecord.size()-1; i++)
 				{
 					dimLblList.add(dimRecord.get(i));
 				}
 				pageDimSet.add(dimLblList);
 			}
 			
 			List<List<Object>> pageDimSummList = new ArrayList<List<Object>>();
 			for (List<Object> dimLabels : pageDimSet)
 			{
 				List<Double> valueList = new ArrayList<Double>();
 				for (List<Object> dimRecord : pageDimList)
 				{
 					boolean getValue = true;
 					for (int i=0; i<dimRecord.size()-1; i++)
 					{
 						if (!dimRecord.get(i).equals(dimLabels.get(i)))
 						{
 							getValue = false;
 							break;
 						}
 					}
 					
 					if (getValue)
 					{
 						double value = 0;
 						try
 						{
 							value = (Double)dimRecord.get(dimRecord.size()-1);
 						}
 						catch (ClassCastException cce)
 						{
 							long lValue = (Long)dimRecord.get(dimRecord.size()-1);
							value = (double) lValue;
 						}
 						valueList.add(value);
 					}
 				}
 				
 				double result = calcFunctionValue(tableSummFuncName, valueList);
 				
 				dimLabels.add(result);
 				pageDimSummList.add(dimLabels);
 			}
 			tblDimSummList.add(pageDimSummList);
 		}
 		
 		return tblDimSummList;
	}
	
	/**
	 * Fetches page-level summary details for a pivot table.
	 * @param	pvtTblData			Complete pivot table data
	 * @param 	tableSummFuncName	Name of the summary function to be applied on the page
	 * @return	Page-level summary details
	 */
	private List<Double> getPageSummary(List<List<List<Object>>> pvtTblData, String tableSummFuncName)
	{
		List<Double> pageSummary = new ArrayList<Double>();
		
		for (List<List<Object>> pageData : pvtTblData)
		{
			List<Double> valueList = new ArrayList<Double>();
			for (List<Object> recordData : pageData)
			{
				double value = 0;
				try
				{
					value = (Double)recordData.get(recordData.size()-1);
				}
				catch (ClassCastException cce)
				{
					long lValue = (Long)recordData.get(recordData.size()-1);
					value = (double) lValue;
				}
				valueList.add(value);
			}
			
			double result = calcFunctionValue(tableSummFuncName, valueList);
			pageSummary.add(result);
		}
		
		return pageSummary;
	}
	
	/**
	 * Fetches table-level summary details of pivot table.
	 * @param 	pvtTblData			Complete pivot table data
	 * @param 	tableSummFuncName	Name of the summary function to be applied on the table
	 * @return	Table-level summary details
	 */
	private double getTableSummary(List<List<List<Object>>> pvtTblData, String tableSummFuncName)
	{
		double tableSummary = 0;
		List<Double> valueList = new ArrayList<Double>();
		
		for (List<List<Object>> pageData : pvtTblData)
		{
			for (List<Object> recordData : pageData)
			{
				double value = 0;
				try
				{
					value = (Double)recordData.get(recordData.size()-1);
				}
				catch (ClassCastException cce)
				{
					long lValue = (Long)recordData.get(recordData.size()-1);
					value = (double) lValue;
				}
				valueList.add(value);
			}
		}
		
		tableSummary = calcFunctionValue(tableSummFuncName, valueList);
		
		return tableSummary;
	}
	
	/**
  	 * Calculates different summary function values.
  	 * @param 	functionName		Name of the function to be calculated
  	 * @param 	valueList			List of values to be used in calculation
  	 * @return	The result calculated
  	 */
  	private double calcFunctionValue(String functionName, List<Double> valueList)
  	{
  		double result = 0;
  		
  		if (functionName.equalsIgnoreCase("Sum"))
  		{
  			result = 0;
  			for (double value : valueList)
  				result += value;
  		}
  		else if (functionName.equalsIgnoreCase("Count"))
  		{
  			result = valueList.size();
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
}
