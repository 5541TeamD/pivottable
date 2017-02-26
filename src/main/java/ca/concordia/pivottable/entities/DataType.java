package ca.concordia.pivottable.entities;

/**
 * Some data types: numeric and string for now.
 * Internally represented as string.
 */
public enum DataType {
    TYPE_NUMERIC("numeric"),
    TYPE_STRING("string");

    private final String val;

    private DataType(final String val) {
        this.val = val;
    }

    @Override
    public String toString() {
       return this.val;
    }
    
    public static DataType getDataType(String val) {
        for (DataType dataType : DataType.values()) {
            if (dataType.val.equals(val)) {
                return dataType;
            }
        }
        return TYPE_STRING;
    }
}
