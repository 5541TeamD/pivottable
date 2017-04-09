package ca.concordia.pivottable.controller;


import ca.concordia.pivottable.servicelayer.SchemaManagementService;
import ca.concordia.pivottable.utils.DependenciesContainer;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllUserSchemas extends Controller {

    private SchemaManagementService service;

    /**
     * All controllers need a constructor that needs the container as argument.
     *
     * @param container The DI container
     */
    public AllUserSchemas(DependenciesContainer container) {
        super(container);
        service = container.get("SchemaManagementService");
    }

    @Override
    protected Object handleAction(Request request, Response response) throws Exception {
        String currentUser = request.session().attribute("username");
        Map<String, Object> ret = new HashMap<>();
        List<String[]> mySchemas = service.getMyOwnedSchemas(currentUser);
        ret.put("mySchemas", mySchemas);
        List<String[]> sharedWithMe = service.getSchemasSharedWithMe(currentUser);
        ret.put("sharedWithMe", sharedWithMe);

        return successObjectResponse(ret, response);
    }
}
