package store.config.service.productService;

import store.config.infra.repository.ProductRepositoryConfig;
import store.service.productService.DefaultProductService;
import store.service.productService.ProductService;

public class ProductServiceConfig {

    private final ProductService productService;

    public ProductServiceConfig(final ProductRepositoryConfig productRepositoryConfig) {
        this.productService = new DefaultProductService(productRepositoryConfig.getProductRepository());
    }

    public ProductService getProductService() {
        return this.productService;
    }
}
