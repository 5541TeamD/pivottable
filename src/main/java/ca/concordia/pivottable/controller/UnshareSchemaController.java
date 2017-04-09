package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.servicelayer.SchemaManagementService;
import ca.concordia.pivottable.utils.DependenciesContainer;
import ca.concordia.pivottable.utils.PivotTableException;
import spark.Request;
import spark.Response;

public class UnshareSchemaController extends Controller
{
	SchemaManagementService service;
	
	/**
	 * All controllers need a constructor that needs the container as argument.
	 *
	 * @param container The DI container
	 */
	public UnshareSchemaController(DependenciesContainer container) 
	{
	    super(container);
	    service = container.get("SchemaManagementService");
	}
    
	@Override
	public Object handleAction(Request request, Response response) throws Exception
	{
		String sharedUsername = request.queryParams("otherUser");
		if (sharedUsername == null)
			throw new PivotTableException("The shared username must be provided.");
		
		String shareableSchemaID = request.queryParams("id");
		if (shareableSchemaID == null)
			throw new PivotTableException("The schema ID must be provided.");
		
		Long schemaId = Long.parseLong(shareableSchemaID, 10);
		service.unshareSchema(sharedUsername, schemaId);
		return successResponse("Schema sharing deleted successfully!", response);
	}
}
