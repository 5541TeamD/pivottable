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
    public PivotTable(PivotTableSchema schema, List<String> pageLabelValues, List<List<List<Object>>> data) {
        this.schema = schema;
        this.pageLabelValues = pageLabelValues;
        this.data = data;
    }

    /**
     * TODO test this
     * @return json representing this entity
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public PivotTableSchema getSchema() {
        return schema;
    }

    public void setSchema(PivotTableSchema schema) {
        this.schema = schema;
    }
    
    public List<String> getPageLabelValues() {
		return pageLabelValues;
	}

	public void setPageLabelValues(List<String> pageLabelValues) {
		this.pageLabelValues = pageLabelValues;
	}

	public void setData(List<List<List<Object>>> data) {
		this.data = data;
	}

	public List<List<List<Object>>> getData() {
        return data;
    }
}
