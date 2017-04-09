package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.entities.ShareableSchema;
import ca.concordia.pivottable.servicelayer.SchemaManagementService;
import ca.concordia.pivottable.utils.DependenciesContainer;
import ca.concordia.pivottable.utils.PivotTableException;
import spark.Request;
import spark.Response;

public class ExportSchemaController extends Controller {

    SchemaManagementService service;

    /**
     * All controllers need a constructor that needs the container as argument.
     *
     * @param container The DI container
     */
    public ExportSchemaController(DependenciesContainer container) {
        super(container);
        this.service = container.get("SchemaManagementService");
    }

    @Override
    protected Object handleAction(Request request, Response response) throws Exception {
        String currentUser = request.session().attribute("username");
        String idStr = request.params("id");
        if (idStr == null) {
            throw new PivotTableException("Schema ID is missing.", 400);
        }
        Long id = Long.parseLong(idStr, 10);
        ShareableSchema schema = service.getSchemaById(id, currentUser);
        response.header("Content-Type", "text/json");
        response.header("Content-Disposition", "attachment; filename=\"" + schema.getSchemaName().replace("\"", "") + ".json\"");
        return toJson(schema, true);
    }
}
