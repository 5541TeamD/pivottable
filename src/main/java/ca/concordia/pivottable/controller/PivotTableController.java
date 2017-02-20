package src.main.java.ca.concordia.pivottable.controller;

public class PivotTableController extends Controller{


    /**
     * All controllers need a constructor that needs the container as argument.
     *
     * @param container The DI container
     */
    public PivotTableController(DependenciesContainer container) {
        super(container);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        //return new Object();
        log.info("Hello!");
        return "Request to handle pivot table schema was received.";
    }
}
