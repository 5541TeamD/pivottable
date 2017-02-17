package ca.concordia.pivottable.servicelayer;

import ca.concordia.pivottable.entities.DataSet;

public interface DataRetrievalService {

    public boolean checkDataSourceConnection(String dataSourceName, String username, String password);

    public DataSet getRawReport(String tableName);

}
