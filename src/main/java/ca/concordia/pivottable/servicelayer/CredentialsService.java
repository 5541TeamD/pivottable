package ca.concordia.pivottable.servicelayer;

/**
 * Stores the data source, username and password
 */
public interface CredentialsService {
    String getDataSource();

    String getUsername();

    String getPassword();

    void setInformation(String dataSource, String username, String password);
}
