package store.config.controller;

import store.config.io.inputHandler.InputHandlerConfig;
import store.config.io.outputHandler.OutputHandlerConfig;
import store.config.service.decisionService.DecisionServiceConfig;
import store.config.service.productService.ProductServiceConfig;
import store.config.service.purchaseService.PurchaseServiceConfig;
import store.controller.Controller;
import store.controller.DefaultController;

public class DefaultControllerConfig implements ControllerConfig {

    private final Controller controller;

    public DefaultControllerConfig(
            final InputHandlerConfig inputHandlerConfig,
            final OutputHandlerConfig outputHandlerConfig,
            final PurchaseServiceConfig purchaseServiceConfig,
            final DecisionServiceConfig decisionServiceConfig,
            final ProductServiceConfig productServiceConfig
    ) {
        this.controller = new DefaultController(
                inputHandlerConfig.getInputHandler(),
                outputHandlerConfig.getOutputHandler(),
                purchaseServiceConfig.getPurchaseService(),
                decisionServiceConfig.getDecisionService(),
                productServiceConfig.getProductService()
        );
    }

    @Override
    public Controller getController() {
        return this.controller;
    }
}
