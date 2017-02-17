package ca.concordia.pivottable.entities;

import com.google.gson.Gson;

import java.util.List;

/**
 * Pivot table entity
 * (used by the UI to render the table)
 */
public class PivotTable {

    private PivotTableSchema schema;

    private List<List<Object>> data;

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
    public PivotTable(PivotTableSchema schema, List<List<Object>> data) {
        this.schema = schema;
        this.data = data;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public PivotTableSchema getSchema() {
        return schema;
    }

    public void setSchema(PivotTableSchema schema) {
        this.schema = schema;
    }

    public List<List<Object>> getData() {
        return data;
    }

    public void setData(List<List<Object>> data) {
        this.data = data;
    }
}
