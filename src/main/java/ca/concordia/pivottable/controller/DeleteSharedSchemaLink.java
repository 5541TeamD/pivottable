package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.servicelayer.SchemaManagementService;
import ca.concordia.pivottable.utils.DependenciesContainer;
import ca.concordia.pivottable.utils.PivotTableException;
import spark.Request;
import spark.Response;

public class DeleteSharedSchemaLink extends Controller {

    private SchemaManagementService service;

    /**
     * All controllers need a constructor that needs the container as argument.
     *
     * @param container The DI container
     */
    public DeleteSharedSchemaLink(DependenciesContainer container) {
        super(container);
        service = container.get("SchemaManagementService");
    }

    @Override
    protected Object handleAction(Request request, Response response) throws Exception {
        String idStr = request.attribute("id");
        if (idStr == null) {
            throw new PivotTableException("Schema id is missing.", 400);
        }
        String currentUser = request.session().attribute("username");
        Long id = Long.parseLong(idStr, 10);
        service.unshareSchema(currentUser, id);

        return successResponse("Removed successfully", response);
    }
}
