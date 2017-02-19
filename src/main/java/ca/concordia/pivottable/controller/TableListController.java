package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.utils.DependenciesContainer;
import spark.Request;
import spark.Response;

/**
 * Handles the request to get the list of tables
 */
public class TableListController extends Controller {


    /**
     * All controllers need a constructor that needs the container as argument.
     *
     * @param container The DI container
     */
    public TableListController(DependenciesContainer container) {
        super(container);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        //return new Object();
        log.info("Hello!");
        return "Request to handle list of tables was received.";
    }
}
