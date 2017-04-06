package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.utils.DependenciesContainer;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        Gson gson = new Gson();
        response.header("content-type", "application/json");
        response.status(status);
        return gson.toJson(map);
    }

}
