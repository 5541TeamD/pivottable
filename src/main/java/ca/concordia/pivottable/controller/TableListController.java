package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.servicelayer.DataRetrievalService;
import ca.concordia.pivottable.utils.DependenciesContainer;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import java.util.List;


/**
 * Handles the request to get the list of tables
 */
public class TableListController extends Controller {
    private DataRetrievalService dataRetrievalService;

    /**
     * All controllers need a constructor that needs the container as argument.
     *
     * @param container The DI container
     */
    public TableListController(DependenciesContainer container) {

        super(container);
        dataRetrievalService = container.get("dataRetrievalService");
    }

    @Override
    public Object handleAction(Request request, Response response) throws Exception {
        //return new Object();
        log.info("TableListController was called.");
        List<String> tableList = dataRetrievalService.getAllRawReportNames();
        response.status(200);
        response.header("Content-Type", "application/json");
        return toJson(tableList);
    }

    private String toJson(List<String> tableList) {
        Gson gson = new Gson();
        return gson.toJson(tableList);
    }
}
