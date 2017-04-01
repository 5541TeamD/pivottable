package ca.concordia.pivottable.entities;

import com.google.gson.Gson;
import java.util.List;

/**
 * Pivot table entity
 * (used by the UI to render the table)
 */
public class PivotTable {

    private PivotTableSchema schema;
    
    private List<String> pageLabelValues;

    private List<List<List<Object>>> data;
    
    private List<List<List<Object>>> rowSummDetails;
	
    private List<List<List<Object>>> colSummDetails;
	
    private List<Double> pageSummDetails;
    
    private double tableSummDetails;

    /**
     * Default constructor
     */
    public PivotTable() {
        // empty
    }

    /**
     * Constructor with parameters
     * @param schema
     * @param data
     */
    public PivotTable(PivotTableSchema schema, List<String> pageLabelValues, List<List<List<Object>>> data,
    					List<List<List<Object>>> rowSummDetails, List<List<List<Object>>> colSummDetails,
    					List<Double> pageSummDetails, double tableSummDetails) {
        this.schema = schema;
        this.pageLabelValues = pageLabelValues;
        this.data = data;
        this.rowSummDetails = rowSummDetails;
        this.colSummDetails = colSummDetails;
        this.pageSummDetails = pageSummDetails;
        this.tableSummDetails = tableSummDetails;
    }

    /**
     * @return json representing this entity
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public PivotTableSchema getSchema() {
        return schema;
    }
    
    public List<String> getPageLabelValues() {
		return pageLabelValues;
	}
    
    public List<List<List<Object>>> getData() {
        return data;
    }

	public List<List<List<Object>>> getRowSummDetails() {
		return rowSummDetails;
	}

	public List<List<List<Object>>> getColSummDetails() {
		return colSummDetails;
	}

	public List<Double> getPageSummDetails() {
		return pageSummDetails;
	}

	public double getTableSummDetails() {
		return tableSummDetails;
	}
	
	public void setSchema(PivotTableSchema schema) {
        this.schema = schema;
    }
    
	public void setPageLabelValues(List<String> pageLabelValues) {
		this.pageLabelValues = pageLabelValues;
	}

	public void setData(List<List<List<Object>>> data) {
		this.data = data;
	}

	public void setRowSummDetails(List<List<List<Object>>> rowSummDetails) {
		this.rowSummDetails = rowSummDetails;
	}

	public void setColSummDetails(List<List<List<Object>>> colSummDetails) {
		this.colSummDetails = colSummDetails;
	}

	public void setPageSummDetails(List<Double> pageSummDetails) {
		this.pageSummDetails = pageSummDetails;
	}

	public void setTableSummDetails(double tableSummDetails) {
		this.tableSummDetails = tableSummDetails;
	}
}
