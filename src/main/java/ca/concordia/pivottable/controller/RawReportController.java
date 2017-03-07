package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.servicelayer.DataRetrievalService;
import ca.concordia.pivottable.utils.DependenciesContainer;
import ca.concordia.pivottable.entities.DataSet;
import spark.Request;
import spark.Response;


public class RawReportController extends Controller{

    /**
     * Services needed
     */
    private DataRetrievalService dataRetrievalService;

    /**
     * All controllers need a constructor that needs the container as argument.
     *
     * @param container The DI container
     */
    public RawReportController(DependenciesContainer container) {

        super(container);
        dataRetrievalService = container.get("dataRetrievalService");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        //return new Object();
        log.info("RawReportController was called");
        String name = request.queryParams("tablename");
		log.info("Tablename is " + name);
        if (name == null) {
            response.status(400);
            return null;
        }
        boolean rawreportexists = dataRetrievalService.rawReportExists(name);
        if (rawreportexists) {
            DataSet rawReport = dataRetrievalService.getRawReport(name);
            response.status(200);
            response.header("Content-Type", "application/json");
            return rawReport.toJSON();
        }else{
            log.info("Raw report does not exist.");
            response.status(404);
            return null;
        }

    }
}
