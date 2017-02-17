package ca.concordia.pivottable.utils;

import ca.concordia.pivottable.controller.Controller;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;

/**
 * This factory builds the controllers
 */
public class ControllerFactory {

    private DependenciesContainer container;

    public ControllerFactory(DependenciesContainer container) {
       this.container = container;
    }

    /**
     *
     * @param clazz The class of the controller to instanciate.
     * @param <T>
     * @return
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
