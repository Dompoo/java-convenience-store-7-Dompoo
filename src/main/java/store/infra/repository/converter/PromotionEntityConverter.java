package store.infra.repository.converter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import store.domain.Product;
import store.infra.entity.PromotionEntity;

public class PromotionEntityConverter {

    public List<PromotionEntity> convert(final List<Product> products) {
        return products.stream()
                .map(PromotionEntity::from)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet())
                .stream()
                .toList();
    }
}
