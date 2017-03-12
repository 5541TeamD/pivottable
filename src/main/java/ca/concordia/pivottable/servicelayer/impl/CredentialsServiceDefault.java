package ca.concordia.pivottable.servicelayer.impl;

import ca.concordia.pivottable.servicelayer.CredentialsService;

/**
 * Default and simple implementation to store credentials.
 */
public class CredentialsServiceDefault implements CredentialsService {

    private String username;
    private String password;
    private String dataSource;

    @Override
    public String getDataSource() {
        return this.dataSource;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setInformation(String dataSource, String username, String password) {
        this.dataSource = dataSource;
        this.username = username;
        this.password = password;
    }
}
