package store.domain.vo;

import java.util.Objects;
import store.common.exception.StoreExceptions;
import store.domain.Product;

final public class PurchaseInfo {

    private final int purchaseAmount;
    private final int promotionStock;
    private final int promotionBuy;
    private final int promotionGet;

    private PurchaseInfo(
            final int purchaseAmount,
            final int promotionStock,
            final int promotionBuy,
            final int promotionGet
    ) {
        validate(purchaseAmount, promotionStock);
        this.purchaseAmount = purchaseAmount;
        this.promotionStock = promotionStock;
        this.promotionBuy = promotionBuy;
        this.promotionGet = promotionGet;
    }

    private void validate(final int purchaseAmount, final int promotionStock) {
        if (purchaseAmount < 0) {
            throw StoreExceptions.ILLEGAL_ARGUMENT.get();
        }
        if (promotionStock < 0) {
            throw StoreExceptions.ILLEGAL_ARGUMENT.get();
        }
    }

    public static PurchaseInfo of(final Product product, final int purchaseAmount) {
        return new PurchaseInfo(
                purchaseAmount,
                product.getPromotionStock(),
                product.getPromotion().getPromotionBuy(),
                product.getPromotion().getPromotionGet()
        );
    }

    public int getPurchaseAmount() {
        return purchaseAmount;
    }

    public int calculatePromotionUnit() {
        return promotionBuy + promotionGet;
    }

    public int getPurchaseAmountWithGetFree() {
        return (purchaseAmount / calculatePromotionUnit() + 1) * calculatePromotionUnit();
    }

    public int getPurchaseAmountWithBringBackNotPromotion() {
        return (calculateStockLimitedPurchaseAmount() / calculatePromotionUnit()) * calculatePromotionUnit();
    }

    public int calculateStockLimitedPurchaseAmount() {
        return Math.min(purchaseAmount, promotionStock);
    }

    public int getPromotionGetWithFullPromotion() {
        return (purchaseAmount / calculatePromotionUnit()) * promotionGet;
    }

    public int getPromotionGetWithGetFree() {
        return ((purchaseAmount / calculatePromotionUnit()) + 1) * promotionGet;
    }

    public int getPromotionGetWithPortionPromotion() {
        return (calculateStockLimitedPurchaseAmount() / calculatePromotionUnit()) * promotionGet;
    }

    public int calculatePromotionedProductAmount(final int promotionGetProductCount) {
        if (promotionGet == 0) {
            return 0;
        }
        return promotionGetProductCount / promotionGet * calculatePromotionUnit();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseInfo purchaseInfo)) {
            return false;
        }
        return purchaseAmount == purchaseInfo.purchaseAmount && promotionStock == purchaseInfo.promotionStock
                && promotionBuy == purchaseInfo.promotionBuy && promotionGet == purchaseInfo.promotionGet;
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseAmount, promotionStock, promotionBuy, promotionGet);
    }
}
