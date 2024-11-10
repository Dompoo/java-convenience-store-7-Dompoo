package store.infra.repository.converter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.domain.Product;
import store.infra.entity.ProductEntity;
import store.infra.entity.PromotionEntity;

public class ProductConverter {

    public List<Product> convert(
            final List<ProductEntity> productEntities,
            final List<PromotionEntity> promotionEntities,
            final LocalDate now
    ) {
        Map<String, PromotionEntity> promotionMap = convertPromotionEntitiesToMap(promotionEntities);
        List<ProductEntity> validProductEntities = withOnlyValidPromotionEntities(productEntities, promotionMap);
        Map<String, ProductBuilder> productMap = new HashMap<>();
        for (ProductEntity productEntity : validProductEntities) {
            productMap.put(productEntity.name(),
                    getUpdatedProductBuilder(now, productEntity, productMap, promotionMap)
            );
        }
        return convertToProducts(productMap);
    }

    private static Map<String, PromotionEntity> convertPromotionEntitiesToMap(
            final List<PromotionEntity> promotionEntities
    ) {
        return promotionEntities.stream()
                .collect(Collectors.toMap(
                        PromotionEntity::name,
                        promotionEntity -> promotionEntity)
                );
    }

    private static List<ProductEntity> withOnlyValidPromotionEntities(
            final List<ProductEntity> productEntities,
            final Map<String, PromotionEntity> promotionMap
    ) {
        return productEntities.stream()
                .filter(productEntity -> !productEntity.isPromotionStockEntity()
                        || promotionMap.containsKey(productEntity.promotionName())).toList();
    }

    private static ProductBuilder getUpdatedProductBuilder(
            final LocalDate now,
            final ProductEntity productEntity,
            final Map<String, ProductBuilder> productBuilderMap,
            final Map<String, PromotionEntity> promotionEntityMap
    ) {
        ProductBuilder productBuilder = createOrGetProductBuilder(productEntity, productBuilderMap, now);
        updateProductBuilder(productEntity, productBuilder, promotionEntityMap);
        return productBuilder;
    }

    private static ProductBuilder createOrGetProductBuilder(
            final ProductEntity productEntity,
            final Map<String, ProductBuilder> products,
            final LocalDate now
    ) {
        String name = productEntity.name();
        if (products.containsKey(name)) {
            return products.get(name);
        }
        return new ProductBuilder(now)
                .setName(productEntity.name())
                .setPrice(productEntity.price());
    }

    private static void updateProductBuilder(
            final ProductEntity productEntity,
            final ProductBuilder productBuilder,
            final Map<String, PromotionEntity> promotionMap
    ) {
        if (productEntity.isPromotionStockEntity()) {
            productBuilder
                    .setPromotionStock(productEntity.quantity())
                    .setPromotion(promotionMap.get(productEntity.promotionName()));
            return;
        }
        productBuilder.setDefaultStock(productEntity.quantity());
    }

    private static List<Product> convertToProducts(
            final Map<String, ProductBuilder> products
    ) {
        return products.values().stream()
                .map(ProductBuilder::build)
                .toList();
    }
}
