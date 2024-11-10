package store.config.infra.database;

import store.infra.database.Database;
import store.infra.entity.ProductEntity;

public interface ProductDatabaseConfig {

    Database<ProductEntity> getProductDatabase();
}
