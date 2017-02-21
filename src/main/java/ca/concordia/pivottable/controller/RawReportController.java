package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.utils.DependenciesContainer;
import spark.Request;
import spark.Response;

public class RawReportController extends Controller{


    /**
     * All controllers need a constructor that needs the container as argument.
     *
     * @param container The DI container
     */
    public RawReportController(DependenciesContainer container) {
        super(container);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        //return new Object();
        log.info("Hello!");
        return "Request to handle raw report data was received.";
    }
}
