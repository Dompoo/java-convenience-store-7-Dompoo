package store.config.controller;

import store.config.aop.RetryHandlerConfig;
import store.config.io.inputHandler.InputHandlerConfig;
import store.controller.Controller;
import store.controller.ControllerRePurchaseProxy;

public class RePurchaseConfig implements ControllerConfig {

    private final Controller controller;

    public RePurchaseConfig(
            final ControllerConfig controllerConfig,
            final InputHandlerConfig inputHandlerConfig,
            final RetryHandlerConfig retryHandlerConfig
    ) {
        this.controller = new ControllerRePurchaseProxy(
                controllerConfig.getController(),
                inputHandlerConfig.getInputHandler(),
                retryHandlerConfig.getRetryHandler()
        );
    }

    @Override
    public Controller getController() {
        return this.controller;
    }
}
