package ca.concordia.pivottable.utils;

/**
 * Custom exception for the project.
 */
public class PivotTableException extends RuntimeException {

    final int statusCode;

    /**
     * Constructor
     * @param message The error details
     * @param statusCode The http status code to send
     */
    public PivotTableException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Another constructor. By default, status code is 500 (Internal server error)
     * @param message The details of the message.
     */
    public PivotTableException(String message) {
        this(message, 500);
    }

    /**
     * Getter for status code
     * @return the http status code for this exception
     */
    public int getStatusCode() {
        return statusCode;
    }

}
