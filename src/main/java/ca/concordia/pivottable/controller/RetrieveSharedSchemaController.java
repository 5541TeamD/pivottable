package ca.concordia.pivottable.controller;

import java.util.List;
import ca.concordia.pivottable.entities.ShareableSchema;
import ca.concordia.pivottable.servicelayer.SchemaManagementService;
import ca.concordia.pivottable.utils.DependenciesContainer;
import ca.concordia.pivottable.utils.PivotTableException;

public class RetrieveSharedSchemaController extends Controller
{
	SchemaManagementService service;
	
	/**
	 * All controllers need a constructor that needs the container as argument.
	 *
	 * @param container The DI container
	 */
	public RetrieveSharedSchemaController(DependenciesContainer container) 
	{
	    super(container);
	    service = container.get("SchemaManagementService");
	}
	
	@Override
	public Object handleAction(Request request, Response response) throws Exception 
	{
	    String currentUser = request.session().attribute("username");
	    List<String[]> sharedSchemaList = service.getMySharedSchemas(currentUser);
	    response.header("Content-Type", "application/json");
	    return toJson(sharedSchemaList);
	}
	
	private String toJson(List<String> sharedSchemaList) {
        Gson gson = new Gson();
        return gson.toJson(sharedSchemaList);
    }
}
