package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.utils.DependenciesContainer;
import ca.concordia.pivottable.utils.PivotTableException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;


/**
 * All controllers will have the DI container injected to them.
 */
public abstract class Controller implements Route {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected DependenciesContainer container;

    /**
     * All controllers need a constructor that needs the container as argument.
     * @param container The DI container
     */
    public Controller(DependenciesContainer container) {
        this.container = container;
    }

    protected String successResponse(String message, Response response) {
        return jsonResponse(message, response, 200);
    }

    protected String jsonResponse(String message, Response response, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("details", message);
        response.header("content-type", "application/json");
        response.status(status);
        return toJson(map);
    }

    protected String successObjectResponse(Object obj, Response response) {
        response.header("Content-Type", "application/json");
        response.status(200);
        return toJson(obj);
    }

    public Object handle(Request request, Response response) throws Exception {
        if (isControllerSecured() && request.session().attribute("username") == null) {
            throw new PivotTableException("Unauthenticated request", 401);
        }
        return handleAction(request, response);
    }

    /**
     * Put any particular action the controller does in this method.
     * @param request The Spark request.
     * @param response The response object.
     * @return Any
     */
    protected abstract Object handleAction(Request request, Response response) throws Exception;

    /**
     * By default all controllers are secured (user must be logged in)
     * If a controller is public, override this method to return true
     * @return
     */
    protected boolean isControllerSecured() {
        return true;
    }

    protected String toJson(Object obj) {
        return toJson(obj, false);
    }

    protected String toJson(Object obj, boolean pretty) {
        Gson gson;
        if (pretty) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        } else {
            gson = new Gson();
        }
        return gson.toJson(obj);
    }



}
