package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.servicelayer.DataRetrievalService;
import ca.concordia.pivottable.utils.DependenciesContainer;
import spark.Request;
import spark.Response;

public class DBConnectionCheckController extends Controller {

    /**
     * Services needed
     */
    private DataRetrievalService dataRetrievalService;

    /**
     * Controller to match parent
     * @param container
     */
    public DBConnectionCheckController(DependenciesContainer container) {
        super(container);
        // TODO this is going to be null
        dataRetrievalService = container.get("dataRetrievalService");
    }

    @Override
    public Object handle(Request request, Response response) {
        log.info("Called the DB Connection check controller");
        // TODO
        return "[\"Hello\"]";
    }
}
