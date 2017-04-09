package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.utils.DependenciesContainer;
import spark.Request;
import spark.Response;
import spark.Session;

public class LogoutController extends Controller {

    /**
     * All controllers need a constructor that needs the container as argument.
     *
     * @param container The DI container
     */
    public LogoutController(DependenciesContainer container) {
        super(container);
    }

    @Override
    public Object handleAction(Request request, Response response) throws Exception {
        Session sess = request.session();
        if (sess.attribute("username") != null) {
            sess.invalidate();
            response.status(200);
            response.header("Content-Type", "application/json");
            return "{\"status\":200,\"message\":\"Logged out successfully.\"}";
        } else {
            return "...ok";
        }
    }

    @Override
    protected boolean isControllerSecured() {
        return false;
    }
}
