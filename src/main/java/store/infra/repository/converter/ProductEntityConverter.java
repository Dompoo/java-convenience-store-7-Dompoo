package store.infra.repository.converter;

import java.util.List;
import store.domain.Product;
import store.infra.entity.ProductEntity;

public class ProductEntityConverter {

    public List<ProductEntity> convert(final List<Product> products) {
        return products.stream()
                .flatMap(ProductEntity::from)
                .toList();
    }
}
