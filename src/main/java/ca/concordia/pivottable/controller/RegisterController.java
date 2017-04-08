package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.entities.ApplicationUser;
import ca.concordia.pivottable.servicelayer.UserManagementService;
import ca.concordia.pivottable.utils.DependenciesContainer;
import spark.Request;
import spark.Response;

public class RegisterController extends Controller {

    /**
     * All controllers need a constructor that needs the container as argument.
     *
     * @param container The DI container
     */
    public RegisterController(DependenciesContainer container) {
        super(container);
    }

    @Override
    public Object handleAction(Request request, Response response) throws Exception {
        UserManagementService service = container.get("UserManagementService");
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        ApplicationUser newUser = new ApplicationUser(username, password);
        service.createUser(newUser);
        return successResponse("User created successfully!", response);
    }
}
