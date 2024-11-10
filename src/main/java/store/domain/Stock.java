package store.domain;

import java.util.Objects;
import store.common.exception.StoreExceptions;

final public class Stock {

    private static final int MIN_DEFAULT_STOCK = 0;
    private static final int MIN_PROMOTION_STOCK = 0;

    private final int defaultStock;
    private final int promotionStock;

    public Stock(final int defaultStock, final int promotionStock) {
        validate(defaultStock, promotionStock);
        this.defaultStock = defaultStock;
        this.promotionStock = promotionStock;
    }

    private static void validate(final int defaultStock, final int promotionStock) {
        if (defaultStock < MIN_DEFAULT_STOCK) {
            throw StoreExceptions.ILLEGAL_ARGUMENT.get();
        }
        if (promotionStock < MIN_PROMOTION_STOCK) {
            throw StoreExceptions.ILLEGAL_ARGUMENT.get();
        }
    }

    public int getDefaultStock() {
        return defaultStock;
    }

    public int getPromotionStock() {
        return promotionStock;
    }

    public int getTotalStock() {
        return defaultStock + promotionStock;
    }

    public Stock withReducing(final int totalDecreaseStock, final int promotionDecreaseStock) {
        int newDefaultStock = defaultStock - (totalDecreaseStock - promotionDecreaseStock);
        int newPromotionStock = promotionStock - promotionDecreaseStock;
        return new Stock(newDefaultStock, newPromotionStock);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stock stock)) {
            return false;
        }
        return defaultStock == stock.defaultStock && promotionStock == stock.promotionStock;
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultStock, promotionStock);
    }
}
