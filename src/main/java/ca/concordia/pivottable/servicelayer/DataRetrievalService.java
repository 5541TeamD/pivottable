package ca.concordia.pivottable.servicelayer;

import ca.concordia.pivottable.entities.DataSet;

/**
 * TODO
 */
public interface DataRetrievalService {

    boolean checkDataSourceConnection(String dataSourceName, String username, String password);

    DataSet getRawReport(String tableName);

}
