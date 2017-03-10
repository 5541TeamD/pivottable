package ca.concordia.pivottable.entities;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * POJO representing a pivot table schema
 */
public class PivotTableSchema {

    private String tableName;
    private List<String> columnLabels;
    private List<String> rowLabels;
    private String pageLabel;
    private String functionName;
    private String valueField;

    public PivotTableSchema() {
        // empty default constructor
    }

    /**
     * Constructor with all parameters provided
     */
    public PivotTableSchema(String table, List<String> columnLabels,
                            List<String> rowLabels, String pageLabel,
                            String functionName, String valueField) {
        this.tableName = table;
        this.columnLabels = columnLabels;
        this.rowLabels = rowLabels;
        this.pageLabel = pageLabel;
        this.functionName = functionName;
        this.valueField = valueField;
    }

    /**
     * Creates an instance of PivotTableSchema from a json string.
     * @param json The string containing the json: note, the server returns it in a data object
     * @return a PivotTableSchema object
     */
    public static PivotTableSchema fromJSON(String json) {
        Gson gson = new Gson();
        JsonObject obj = gson.fromJson(json, JsonObject.class);
        return gson.fromJson(obj.get("data"), PivotTableSchema.class);
    }

    // Getters and setters


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumnLabels() {
        return columnLabels;
    }

    public void setColumnLabels(List<String> columnLabels) {
        this.columnLabels = columnLabels;
    }

    public List<String> getRowLabels() {
        return rowLabels;
    }

    public void setRowLabels(List<String> rowLabels) {
        this.rowLabels = rowLabels;
    }

    public String getPageLabel() {
        return pageLabel;
    }

    public void setPageLabel(String pageLabel) {
        this.pageLabel = pageLabel;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

}
