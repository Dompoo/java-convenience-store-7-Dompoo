package store.config.service.decisionService;

import store.config.aop.RetryHandlerConfig;
import store.config.infra.repository.ProductRepositoryConfig;
import store.service.decisionService.DecisionService;
import store.service.decisionService.DecisionServiceRetryProxy;
import store.service.decisionService.DefaultDecisionService;

public class DecisionServiceConfig {

    private final DecisionService decisionService;

    public DecisionServiceConfig(
            final ProductRepositoryConfig productRepositoryConfig,
            final RetryHandlerConfig retryHandlerConfig
    ) {
        DefaultDecisionService defaultDecisionService = new DefaultDecisionService(
                productRepositoryConfig.getProductRepository()
        );
        this.decisionService = new DecisionServiceRetryProxy(
                defaultDecisionService,
                retryHandlerConfig.getRetryHandler()
        );
    }

    public DecisionService getDecisionService() {
        return this.decisionService;
    }
}
