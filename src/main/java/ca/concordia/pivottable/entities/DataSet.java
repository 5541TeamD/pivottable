package ca.concordia.pivottable.entities;

import com.google.gson.Gson;

import java.util.*;

/**
 * TODO: Remove this. This is from the prototype
 */
public class DataSet {
    List<DataField> columns;
    List<List<Object>> rows;

    public DataSet() {
        columns = new ArrayList<>();
        rows = new ArrayList<>();
    }

    public DataSet(List<DataField> columns, List<List<Object>> data) {
        this.rows = data;
        this.columns = columns;
    }

    public List<Object> getRowAt(int index) {
        return rows.get(index);
    }

    public void setColumns(List<DataField> fields) {
        this.columns = fields;
    }

    public void addField(DataField field) {
        this.columns.add(field);
    }

    public void insertRow(List<Object> row) {
        this.rows.add(row);
    }

    public List<List<Object>> getRows() {
        return rows;
    }

    public List<DataField> getColumns() {
        return columns;
    }

    public int getSize() {
        return rows.size();
   }

   /**
    * TODO test this
    * @return json representing this entity
    */
   public String toJSON() {
       Gson gson = new Gson();
       return gson.toJson(this);
   }

}
