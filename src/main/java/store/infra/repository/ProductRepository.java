package store.infra.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.common.exception.StoreExceptions;
import store.domain.Product;
import store.infra.database.Database;
import store.infra.entity.ProductEntity;
import store.infra.entity.PromotionEntity;
import store.infra.repository.converter.ProductConverter;
import store.service.dateProvider.DateProvider;

public class ProductRepository implements Repository<Product> {

    private final List<Product> products = new ArrayList<>();

    public ProductRepository(
            final Database<ProductEntity> productDatabase,
            final Database<PromotionEntity> promotionDatabase,
            final ProductConverter productConverter,
            final DateProvider dateProvider
    ) {
        List<PromotionEntity> promotionEntities = getUniquePromotionEntities(promotionDatabase, dateProvider.getDate());
        this.products.addAll(
                productConverter.convert(productDatabase.readAll(), promotionEntities)
        );
    }

    private static List<PromotionEntity> getUniquePromotionEntities(
            final Database<PromotionEntity> promotionDatabase,
            final LocalDate now
    ) {
        return promotionDatabase.readAll().stream()
                .filter(promotionEntity -> promotionEntity.isAvailable(now))
                .toList();
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public Optional<Product> findByName(final String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(final Product product) {
        Product findProduct = products.stream()
                .filter(products -> products.getName().equals(product.getName()))
                .findFirst()
                .orElseThrow(StoreExceptions.PRODUCT_NOT_FOUND::get);

        products.remove(findProduct);
        products.add(product);
    }
}
