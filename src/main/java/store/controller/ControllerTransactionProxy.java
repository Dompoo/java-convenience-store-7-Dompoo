package store.controller;

import java.util.List;
import store.infra.repository.Repository;

public class ControllerTransactionProxy implements Controller {

    private final Controller targetController;
    private final List<Repository<?>> repositories;

    public ControllerTransactionProxy(
            final Controller targetController,
            final List<Repository<?>> repositories
    ) {
        this.targetController = targetController;
        this.repositories = repositories;
    }

    @Override
    public void run() {
        targetController.run();
        for (Repository<?> repository : repositories) {
            repository.updateDatabaseInBatch();
        }
    }
}
