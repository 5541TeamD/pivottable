package ca.concordia.pivottable.entities;

import com.google.gson.Gson;

/**
 * DataField class. It represents a column in a table.
 */
public class DataField {
    private final String name;
    private final DataType type;
    private final String alias;

    public DataField(String name, DataType type, String alias) {
        this.name = name;
        this.type = type;
        this.alias = alias;
    }

    public DataField(String name, DataType type) {
        // make alias the same as name
        this(name, type, name);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (other.getClass() != this.getClass()) {
            return false;
        } else {
            DataField o = (DataField)other;
            return this.name.equals(o.name);
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    String getName() {
        return name;
    }

    DataType getType() {
        return type;
    }

    String getAlias() {
        return alias;
    }

    String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
