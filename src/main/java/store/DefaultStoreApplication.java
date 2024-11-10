package store;

import store.config.aop.RetryHandlerConfig;
import store.config.controller.ControllerConfig;
import store.config.controller.DefaultControllerConfig;
import store.config.controller.RePurchaseConfig;
import store.config.controller.TransactionConfig;
import store.config.infra.database.ProductFileDatabaseConfig;
import store.config.infra.database.PromotionFileDatabaseConfig;
import store.config.infra.repository.DefaultProductRepositoryConfig;
import store.config.infra.repository.ProductRepositoryConfig;
import store.config.infra.repository.RepositoriesConfig;
import store.config.io.inputHandler.InputHandlerConfig;
import store.config.io.outputHandler.OutputHandlerConfig;
import store.config.io.reader.MissionUtilsReaderConfig;
import store.config.io.writer.SystemWriterConfig;
import store.config.io.writer.WriterConfig;
import store.config.service.dateProvider.DateProviderConfig;
import store.config.service.dateProvider.MissionUtilsDateProviderConfig;
import store.config.service.decisionService.DecisionServiceConfig;
import store.config.service.productService.ProductServiceConfig;
import store.config.service.purchaseService.PurchaseServiceConfig;
import store.controller.Controller;

public class DefaultStoreApplication implements StoreApplication {

    private final Controller controller;

    public DefaultStoreApplication() {
        ConfigurationContext context = buildConfigurationContext();
        this.controller = buildController(context);
    }

    private ConfigurationContext buildConfigurationContext() {
        WriterConfig writerConfig = new SystemWriterConfig();
        DateProviderConfig dateProvider = new MissionUtilsDateProviderConfig();
        InputHandlerConfig inputHandler = new InputHandlerConfig(new MissionUtilsReaderConfig(), writerConfig);
        ProductRepositoryConfig productRepository = buildProductRepository(dateProvider);
        RetryHandlerConfig retryHandler = new RetryHandlerConfig(writerConfig);

        return new ConfigurationContext(writerConfig, dateProvider, inputHandler, productRepository, retryHandler);
    }

    private ProductRepositoryConfig buildProductRepository(final DateProviderConfig dateProvider) {
        return new DefaultProductRepositoryConfig(
                new ProductFileDatabaseConfig(),
                new PromotionFileDatabaseConfig(),
                dateProvider
        );
    }

    private Controller buildController(final ConfigurationContext context) {
        ControllerConfig baseController = buildBaseController(context);
        ControllerConfig rePurchaseController = buildRePurchaseController(context, baseController);
        return buildTransactionController(context, rePurchaseController).getController();
    }

    private ControllerConfig buildBaseController(final ConfigurationContext context) {
        return new DefaultControllerConfig(
                context.inputHandler(),
                new OutputHandlerConfig(context.writer()),
                new PurchaseServiceConfig(context.productRepository(), context.retryHandler()),
                new DecisionServiceConfig(context.productRepository(), context.retryHandler()),
                new ProductServiceConfig(context.productRepository())
        );
    }

    private ControllerConfig buildRePurchaseController(
            final ConfigurationContext context,
            final ControllerConfig baseController
    ) {
        return new RePurchaseConfig(
                baseController,
                context.inputHandler(),
                context.retryHandler()
        );
    }

    private ControllerConfig buildTransactionController(
            final ConfigurationContext context,
            final ControllerConfig rePurchaseController
    ) {
        return new TransactionConfig(
                rePurchaseController,
                new RepositoriesConfig(context.productRepository())
        );
    }

    @Override
    public void run() {
        this.controller.run();
    }

    private record ConfigurationContext(
            WriterConfig writer,
            DateProviderConfig dateProvider,
            InputHandlerConfig inputHandler,
            ProductRepositoryConfig productRepository,
            RetryHandlerConfig retryHandler
    ) {
    }
}
