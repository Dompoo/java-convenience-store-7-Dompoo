package store.config.infra.repository;

import store.domain.Product;
import store.infra.repository.Repository;

public interface ProductRepositoryConfig {

    Repository<Product> getProductRepository();
}
