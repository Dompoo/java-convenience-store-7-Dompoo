package store.config.infra.repository;

import store.config.infra.database.ProductDatabaseConfig;
import store.config.infra.database.PromotionDatabaseConfig;
import store.config.service.dateProvider.DateProviderConfig;
import store.domain.Product;
import store.infra.repository.ProductRepository;
import store.infra.repository.Repository;
import store.infra.repository.converter.ProductConverter;
import store.infra.repository.converter.ProductEntityConverter;
import store.infra.repository.converter.PromotionEntityConverter;

public class DefaultProductRepositoryConfig implements ProductRepositoryConfig {

    private final Repository<Product> productRepository;

    public DefaultProductRepositoryConfig(
            final ProductDatabaseConfig productDatabaseConfig,
            final PromotionDatabaseConfig promotionDatabaseConfig,
            final DateProviderConfig dateProviderConfig
    ) {
        this.productRepository = new ProductRepository(
                productDatabaseConfig.getProductDatabase(),
                promotionDatabaseConfig.getPromotionDatabase(),
                new ProductConverter(),
                new ProductEntityConverter(),
                new PromotionEntityConverter(),
                dateProviderConfig.getDateProvider()
        );
    }

    @Override
    public Repository<Product> getProductRepository() {
        return this.productRepository;
    }
}
