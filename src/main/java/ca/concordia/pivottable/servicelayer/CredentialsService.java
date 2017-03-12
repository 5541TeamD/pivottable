package ca.concordia.pivottable.servicelayer;

/**
 * Stores the data source, username and password
 */
public interface CredentialsService {

    /**
     * The data source url
     * @return String (example: jdbc:mysql://localhost:3306/testdb)
     */
    String getDataSource();

    /**
     * The username
     * @return The database username
     */
    String getUsername();

    /**
     * The password to access the database
     * @return String or null
     */
    String getPassword();

    /**
     * Setter for setting information about the data source.
     * @param dataSource The data source url
     * @param username The username
     * @param password The password
     */
    void setInformation(String dataSource, String username, String password);
}
