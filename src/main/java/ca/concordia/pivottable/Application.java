package ca.concordia.pivottable;

import ca.concordia.pivottable.controller.Controller;
import ca.concordia.pivottable.controller.DBConnectionCheckController;
import ca.concordia.pivottable.controller.TableListController;
import ca.concordia.pivottable.utils.ControllerFactory;
import ca.concordia.pivottable.utils.DependenciesContainer;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Application {

    // define all API endpoints in this map
    // with the associated controller.
    private static final Map<String, Class> GET;
    static
    {
        GET = new HashMap<>();
        // GET endpoints
        GET.put("/api/checkaccess", DBConnectionCheckController.class);
        GET.put("/api/tables", TableListController.class);
    }

    private static final Map<String, Class> POST;
    static
    {
        // "/api" POST endpoints
        POST = new HashMap<>();
        // POST end points.
    }


    public static void main(String[] args) {

        System.out.println("Application running at http://localhost:4567.");

        staticFiles.location("/ui/build");

        before((request, response) -> {
            DependenciesContainer container = new DependenciesContainer();
            request.attribute("container", container);
        });

        defineRoutes();

    }

    /**
     * Defines the end points.
     * This is where the controllers get instanciated.
     */
    @SuppressWarnings("unchecked")
    private static void defineRoutes() {
        get("/hello", (req, res) -> {
            return "Hello, World!";
        } );

        get("/stop", (req, res) -> {
            stop();
            return "";
        });

       for (Map.Entry<String, Class> entry : GET.entrySet()) {
           get(entry.getKey(), ((request, response) -> {
               DependenciesContainer container = request.attribute("container");
               ControllerFactory factory = container.get("ControllerFactory");
               Controller ctrl = factory.getController(entry.getValue());
               return ctrl.handle(request, response);
           }));
       }
    }
}
