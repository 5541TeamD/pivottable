package ca.concordia.pivottable.utils;

public class ErrorResponse {

    private final int status;
    private final String details;

    public ErrorResponse(String message, int status) {
        this.details = message;
        this.status = status;
    }

    public ErrorResponse(String message) {
        this(message, 500);
    }

    public String getDetails() {
        return this.details;
    }

    public int getStatus() {
        return this.status;
    }

}
