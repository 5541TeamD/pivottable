package ca.concordia.pivottable;

import ca.concordia.pivottable.controller.*;
import ca.concordia.pivottable.servicelayer.CredentialsService;
import ca.concordia.pivottable.utils.*;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Application {

    private static Logger log = LoggerFactory.getLogger(Application.class);
    /**
     * Interface representing the kind of arguments a Spark route function takes
     */
    @FunctionalInterface
    interface RouteFunction {
       void call(final String path, final Route route);
    }

    // define all API endpoints in this map
    // with the associated controller.
    private static final Map<String, Class> GET;
    static
    {
        GET = new HashMap<>();
        // GET endpoints
        GET.put("/api/checkaccess", DBConnectionCheckController.class);
        GET.put("/api/tables", TableListController.class);
        GET.put("/api/rawreport", RawReportController.class);
        GET.put("/api/login", LoginController.class);
        GET.put("/api/logout", LogoutController.class);
        GET.put("/api/register", RegisterController.class);
        GET.put("/api/userinfo", UserInfoController.class);
    }

    private static final Map<String, Class> POST;
    static
    {
        // "/api" POST endpoints
        POST = new HashMap<>();
        // POST end points. This one is post because there is data in the body provided by the client.
        POST.put("/api/pivottable", PivotTableController.class);
    }

    private static final String[] UIRoutes = {
            "/login",
            "/home",
            "/create",
            "/edit/*",
            "/view/*"
    };

    /**
     * Main entry point of the application
     * @param args
     */
    public static void main(String[] args) {

        System.out.println("Application running at http://localhost:4567.");
        staticFiles.location("/ui/build");

        // Dependency injection container is request scoped
        before((request, response) -> {
            DependenciesContainer container = new DependenciesContainer();
            request.attribute("container", container);
            CredentialsService credentials = container.get("CredentialsService");
            String userName = request.headers("username");
            String password = request.headers("password");
            String dataSource = request.headers("jdbcUrl");
            credentials.setInformation(dataSource, userName, password);
        });

        defineRoutes();

        defineExceptionHandlers();

    }

    /**
     * Defines the end points.
     * This is where the controllers get instanciated.
     */
    private static void defineRoutes() {
        get("/hello", (req, res) -> {
            return "Hello, World!";
        } );

        addUIRoutes();

        get("/stop", (req, res) -> {
            stop();
            return "";
        });

        addMapHandler(GET, Spark::get);
        addMapHandler(POST, Spark::post);

    }


    /**
     * Private method to initialize the route handlers
     * @param map the map
     * @param method the method
     */
    @SuppressWarnings("unchecked")
    private static void addMapHandler(final Map<String, Class> map, final RouteFunction method) {
        for (Map.Entry<String, Class> entry : map.entrySet()) {
            method.call(entry.getKey(), ((Request request, Response response) -> {
                DependenciesContainer container = request.attribute("container");
                ControllerFactory factory = container.get("ControllerFactory");
                Controller ctrl = factory.getController(entry.getValue());
                return ctrl.handle(request, response);
            }));
        }
    }

    /**
     * In order to handle browserHistory HTML5 API,
     * the server needs to render the same view for different kinds of routes.
     */
    private static void addUIRoutes() {
        for (String route : UIRoutes) {
            get(route, Application::renderView);
        }
    }

    /**
     * Defines an exception handler to return something meaningful to the client.
     * The error code for the general exception is 500.
     */
    private static void defineExceptionHandlers() {
        exception(Exception.class, (e, req, res) -> {
            log.error("Message from General Exception handler: " + e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 500);
            res.header("Content-Type", "application/json");
            res.status(500);
            Gson gson = new Gson();
            res.body(gson.toJson(errorResponse));
        });
    }

    /**
     * There is only a single html file to render.
     * @param req
     * @param resp
     * @return contents of html file as string.
     */
    private static Object renderView(Request req, Response resp) {
        try {
            return FileUtils.readFileContents("/ui/build/index.html");
        } catch (IOException ioe) {
            final String error = "Project may have not been built correctly. Cannot render index.html";
            log.error(ioe.getMessage());
            ioe.printStackTrace();
            throw new PivotTableException(error);
        }
    }
}
