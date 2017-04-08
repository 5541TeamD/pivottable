package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.entities.ShareableSchema;
import ca.concordia.pivottable.servicelayer.SchemaManagementService;
import ca.concordia.pivottable.utils.DependenciesContainer;
import ca.concordia.pivottable.utils.PivotTableException;
import spark.Request;
import spark.Response;

public class RetrieveSchemaController extends Controller {

    SchemaManagementService service;

    /**
     * All controllers need a constructor that needs the container as argument.
     *
     * @param container The DI container
     */
    public RetrieveSchemaController(DependenciesContainer container) {
        super(container);
        service = container.get("SchemaManagementService");
    }

    @Override
    public Object handleAction(Request request, Response response) throws Exception {
        String idStr = request.params("id");
        if (idStr == null) {
            throw new PivotTableException("The schema id must be provided.");
        }
        String currentUser = request.session().attribute("username");
        Long id = Long.parseLong(idStr, 10);
        ShareableSchema schema = service.getSchemaById(id, currentUser);
        response.header("Content-Type", "application/json");
        return schema.toJSON();
    }
}
