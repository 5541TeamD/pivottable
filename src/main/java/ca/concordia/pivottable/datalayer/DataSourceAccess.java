package ca.concordia.pivottable.datalayer;

import java.util.List;

public interface DataSourceAccess {

    void connect();

    void disconnect();

    List<String> getAllTableNames();

}
