package store.controller;

import store.aop.RetryHandler;
import store.io.input.InputHandler;

public class ControllerRePurchaseProxy implements Controller {

    private final Controller targetController;
    private final InputHandler inputHandler;
    private final RetryHandler retryHandler;

    public ControllerRePurchaseProxy(
            final Controller targetController,
            final InputHandler inputHandler,
            final RetryHandler retryHandler
    ) {
        this.targetController = targetController;
        this.inputHandler = inputHandler;
        this.retryHandler = retryHandler;
    }

    @Override
    public void run() {
        do {
            targetController.run();
        } while (retryHandler.tryUntilSuccess(inputHandler::handleRePuchase));
    }
}
