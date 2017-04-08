package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.entities.PivotTable;
import ca.concordia.pivottable.entities.PivotTableSchema;
import ca.concordia.pivottable.servicelayer.DataRetrievalService;
import ca.concordia.pivottable.utils.DependenciesContainer;
import ca.concordia.pivottable.entities.DataSet;
import ca.concordia.pivottable.entities.PivotTableSchema;
import spark.Request;
import spark.Response;

public class PivotTableController extends Controller {
    private DataRetrievalService dataRetrievalService;

    /**
     * All controllers need a constructor that needs the container as argument.
     *
     * @param container The DI container
     */
    public PivotTableController(DependenciesContainer container) {

        super(container);
        dataRetrievalService = container.get("dataRetrievalService");
    }

    @Override
    public Object handleAction(Request request, Response response) throws Exception {
        //return new Object();
        log.info("PivotTableController was called");
        String name = request.params("pvtTblSchema");
        PivotTableSchema schema = PivotTableSchema.fromJSON(request.body());
        if (schema == null){
            response.status(400);
            return null;
        }
        PivotTable pivotTable = dataRetrievalService.getPivotTable(schema);
        response.status(200);
        response.header("Content-Type", "application/json");
        return pivotTable.toJSON();
    }
}

