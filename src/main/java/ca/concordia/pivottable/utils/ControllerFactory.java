package ca.concordia.pivottable.utils;

import ca.concordia.pivottable.controller.Controller;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;

/**
 * This factory builds the controllers
 */
public class ControllerFactory {

    private DependenciesContainer container;

    /**
     * Constructor to match the parent one. It takes a DependenciesContainer instance
     * @param container
     */
    public ControllerFactory(DependenciesContainer container) {
       this.container = container;
    }

    /**
     * Main method of the controller factory.
     * @param clazz The class of the controller to instantiate.
     * @param <T> The controller class
     * @return An instance of the controller
     */
    public <T extends Controller> T getController(Class<T> clazz) {
        Logger log = (Logger)container.get("logger");
        try {
            return clazz.getDeclaredConstructor(DependenciesContainer.class).newInstance(container);
        } catch (NoSuchMethodException|IllegalAccessException|InstantiationException|InvocationTargetException e) {
            log.error(e.getMessage());
            throw new PivotTableException(e.getMessage());
        }
    }

}
