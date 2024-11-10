package store.config.infra.database;

import store.infra.database.Database;
import store.infra.database.ProductFileDatabase;
import store.infra.entity.ProductEntity;

public class ProductFileDatabaseConfig implements ProductDatabaseConfig {

    private final Database<ProductEntity> productDatabase;

    public ProductFileDatabaseConfig() {
        this.productDatabase = new ProductFileDatabase();
    }

    @Override
    public Database<ProductEntity> getProductDatabase() {
        return this.productDatabase;
    }
}
