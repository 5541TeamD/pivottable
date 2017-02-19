package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.utils.DependenciesContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;


/**
 * All controlers will have the DI container injected to them.
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

}
