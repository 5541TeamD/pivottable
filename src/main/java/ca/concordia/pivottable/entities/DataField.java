package ca.concordia.pivottable.entities;

/**
 * TODO: Remove this. This is from the prototype
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
        this(name, type, "");
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
}
