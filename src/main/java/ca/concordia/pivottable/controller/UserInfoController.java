package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.utils.DependenciesContainer;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class UserInfoController extends Controller {
    /**
     * All controllers need a constructor that needs the container as argument.
     *
     * @param container The DI container
     */
    public UserInfoController(DependenciesContainer container) {
        super(container);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String user = request.session().attribute("username");
        if (user == null) {
           user = "";
        }
        return successResponse(user, response);
    }
}
