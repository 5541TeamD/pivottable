package ca.concordia.pivottable.entities;

import java.io.Serializable;
import java.util.*;

/**
 * TODO: Remove this. This is from the prototype
 */
public class DataSet implements Serializable {
   Set<DataField> columns;
   List<Map<DataField, Object>> rows;

   public DataSet() {
      columns = new HashSet<>();
      rows = new ArrayList<>();
   }

   public DataSet(Set<DataField> columns, List<Map<DataField, Object>> data) {
      this.rows = data;
      this.columns = columns;
   }

   public Map<DataField, Object> getRowAt(int index) {
      return rows.get(index);
   }

   public void setColumns(Set<DataField> fields) {
      this.columns = fields;
   }

   public void addField(DataField field) {
      this.columns.add(field);
   }

   public void insertRow(Map<DataField, Object> row) {
      this.rows.add(row);
   }

   public List<Map<DataField, Object>> getRows() {
      return rows;
   }

   public Set<DataField> getColumns() {
      return columns;
   }

   public int getSize() {
     return rows.size();
   }

}
