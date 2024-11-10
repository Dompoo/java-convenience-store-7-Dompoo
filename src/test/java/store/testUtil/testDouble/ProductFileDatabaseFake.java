package store.testUtil.testDouble;

import java.util.ArrayList;
import java.util.List;
import store.infra.database.Database;
import store.infra.entity.ProductEntity;

public class ProductFileDatabaseFake implements Database<ProductEntity> {

    private final List<ProductEntity> productEntities = new ArrayList<>();

    public void setUpProductEntities(List<ProductEntity> productEntities) {
        this.productEntities.addAll(productEntities);
    }

    @Override
    public List<ProductEntity> readAll() {
        return productEntities;
    }

    @Override
    public void updateAll(List<ProductEntity> objects) {
    }
}
