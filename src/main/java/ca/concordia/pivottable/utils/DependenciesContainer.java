package ca.concordia.pivottable.utils;

import ca.concordia.pivottable.datalayer.impl.DataSourceAccessImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class DependenciesContainer {

    private HashMap<String, Object> container;
    private Logger log;

    public DependenciesContainer() {
        container = new HashMap<>();
        wireDependencies();
    }

    private void wireDependencies() {
        log = LoggerFactory.getLogger(DependenciesContainer.class);
        container.put("logger", log);
    }

    /**
     * Creates a new object with dependencies wired.
     * Developer should specify how the dependency should be used.
     * This container creates a single instance for the life of the container.
     * @param name name of the class to use
     * @return An instance of the given class name
     * @throws InstantiationException when the class has not been wired yet.
     */
    private Object wireDependency(String name) throws InstantiationException {
        switch (name.toLowerCase()) {
            case "logger":
                return this.log;
            case "controllerfactory":
                return new ControllerFactory(this);
            // TODO maybe use factory method pattern if many classes have the same dependency.
            case "datasourceaccess":
                return new DataSourceAccessImpl(/*get("dependency1")*/);
            default:
                throw new InstantiationException("No dependency wired " + name + ". Developer needs to specify this.");
        }
    }

    /**
     * Get your dependency by passing the class name you want
     * Assign the return of this method to a variable.
     * @param className The name of the class you want.
     * @param <T> The class you want.
     * @return An instance of the class you want.
     */
    public <T> T get(String className) {
        String lowercasename = className.toLowerCase();
        Object dependency = container.get(lowercasename);
        if (dependency == null) {
            try {
                dependency = wireDependency(lowercasename);
                container.put(lowercasename, dependency);
            } catch (InstantiationException ex) {
                log.warn(ex.getMessage());
                //throw new PivotTableException(ex.getMessage());
            }
        }
        return dependency == null ? null : (T)dependency;
    }

}