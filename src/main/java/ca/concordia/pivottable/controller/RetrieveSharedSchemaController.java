package ca.concordia.pivottable.controller;

import java.util.List;
import ca.concordia.pivottable.servicelayer.SchemaManagementService;
import ca.concordia.pivottable.utils.DependenciesContainer;
import spark.Request;
import spark.Response;

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
}
