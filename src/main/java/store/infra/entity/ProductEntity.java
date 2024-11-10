package store.infra.entity;

import java.util.Map;
import java.util.Objects;

public record ProductEntity(
        String name,
        int price,
        int quantity,
        String promotionName
) implements DatabaseEntity {

    private static final String NAME_COLUMN = "name";
    private static final String PRICE_COLUMN = "price";
    private static final String QUANTITY_COLUMN = "quantity";
    private static final String PROMOTION_NAME_COLUMN = "promotion";
    private static final String NO_PROMOTION_PROMOTION_NAME = "null";

    public static ProductEntity from(final Map<String, String> dataMap) {
        return new ProductEntity(
                dataMap.get(NAME_COLUMN),
                Integer.parseInt(dataMap.get(PRICE_COLUMN)),
                Integer.parseInt(dataMap.get(QUANTITY_COLUMN)),
                dataMap.get(PROMOTION_NAME_COLUMN)
        );
    }

    public boolean isPromotionStockEntity() {
        return promotionName != null
                && !promotionName.equals(NO_PROMOTION_PROMOTION_NAME)
                && !promotionName.isBlank();
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
