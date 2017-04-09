package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.entities.ShareableSchema;
import ca.concordia.pivottable.servicelayer.SchemaManagementService;
import ca.concordia.pivottable.utils.DependenciesContainer;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class SavePivotTableSchemaController extends Controller {

    SchemaManagementService service;

    /**
     * All controllers need a constructor that needs the container as argument.
     *
     * @param container The DI container
     */
    public SavePivotTableSchemaController(DependenciesContainer container) {
        super(container);

        service = container.get("SchemaManagementService");
    }

    @Override
    public Object handleAction(Request request, Response response) throws Exception {

        ShareableSchema schema = ShareableSchema.fromJSon(request.body());
        schema.setOwnerUsername(request.session().attribute("username"));
        Long ret;
        if (schema.getSchemaID() == null) {
            ret = service.createShareableSchema(schema);
        } else {
            service.updateShareableSchema(schema);
            ret = schema.getSchemaID();
        }
        return successResponse(ret.toString(), response);
    }
}
