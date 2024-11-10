package store.testUtil.testDouble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import store.domain.Product;
import store.infra.repository.Repository;

public class ProductRepositoryFake implements Repository<Product> {

    private final List<Product> products = new ArrayList<>();

    public void setProducts(Product... products) {
        this.products.addAll(Arrays.asList(products));
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public Optional<Product> findByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst();
    }

    @Override
    public void update(Product product) {
    }

    @Override
    public void updateDatabaseInBatch() {
    }
}
