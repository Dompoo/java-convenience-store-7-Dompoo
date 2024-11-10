package store.infra.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Stream;
import store.domain.Product;

public record ProductEntity(
        String name,
        int price,
        int quantity,
        String promotionName
) implements DatabaseEntity {

    public static ProductEntity from(final Map<String, String> dataMap) {
        String name = dataMap.get("name");
        int price = Integer.parseInt(dataMap.get("price"));
        int stock = Integer.parseInt(dataMap.get("quantity"));
        String promotionName = dataMap.get("promotion");
        if (promotionName.equals("null")) {
            return new ProductEntity(name, price, stock, "");
        }
        return new ProductEntity(name, price, stock, promotionName);
    }

    public boolean isPromotionStockEntity() {
        return promotionName != null && !promotionName.isBlank();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductEntity that)) {
            return false;
        }
        return price == that.price && Objects.equals(name, that.name) && Objects.equals(promotionName,
                that.promotionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, promotionName);
    }
}
