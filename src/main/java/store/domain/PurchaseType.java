package store.domain;

import store.domain.vo.PurchaseInfo;
import store.domain.vo.PurchaseStatus;

public enum PurchaseType {

    FULL_DEFAULT(
            PurchaseInfo::getPurchaseAmount,
            (purchaseInfo) -> 0,
            (purchaseInfo) -> 0
    ),
    FULL_PROMOTION(
            PurchaseInfo::getPurchaseAmount,
            PurchaseInfo::getPromotionGetWithFullPromotion,
            PurchaseInfo::getPurchaseAmount
    ),
    FULL_PROMOTION_BRING_FREE(
            PurchaseInfo::getPurchaseAmountWithGetFree,
            PurchaseInfo::getPromotionGetWithGetFree,
            PurchaseInfo::getPurchaseAmountWithGetFree
    ),
    FULL_PROMOTION_NOT_BRING_FREE(
            PurchaseInfo::getPurchaseAmount,
            PurchaseInfo::getPromotionGetWithFullPromotion,
            PurchaseInfo::getPurchaseAmount
    ),
    PORTION_PROMOTION_BRING_BACK(
            PurchaseInfo::getPurchaseAmountWithBringBackNotPromotion,
            PurchaseInfo::getPromotionGetWithPortionPromotion,
            PurchaseInfo::getPurchaseAmountWithBringBackNotPromotion
    ),
    PORTION_PROMOTION_NOT_BRING_BACK(
            PurchaseInfo::getPurchaseAmount,
            PurchaseInfo::getPromotionGetWithPortionPromotion,
            PurchaseInfo::calculateStockLimitedPurchaseAmount
    ),
    ;

    private final StoreCalculator<Integer> finalPurchaseProductCountCalculator;
    private final StoreCalculator<Integer> promotionGetProductCountCalculator;
    private final StoreCalculator<Integer> promotionStockToDecreaseCalculator;

    PurchaseType(
            StoreCalculator<Integer> finalPurchaseProductCountCalculator,
            StoreCalculator<Integer> promotionGetProductCountCalculator,
            StoreCalculator<Integer> promotionStockToDecreaseCalculator
    ) {
        this.finalPurchaseProductCountCalculator = finalPurchaseProductCountCalculator;
        this.promotionGetProductCountCalculator = promotionGetProductCountCalculator;
        this.promotionStockToDecreaseCalculator = promotionStockToDecreaseCalculator;
    }

    public PurchaseStatus proceed(final PurchaseInfo purchaseInfo) {
        Integer promotionGetProductCount = this.promotionGetProductCountCalculator.calculate(purchaseInfo);
        return new PurchaseStatus(
                this.finalPurchaseProductCountCalculator.calculate(purchaseInfo),
                promotionGetProductCount,
                this.promotionStockToDecreaseCalculator.calculate(purchaseInfo),
                purchaseInfo.calculatePromotionedProductAmount(promotionGetProductCount)
        );
    }

    @FunctionalInterface
    public interface StoreCalculator<T> {
        T calculate(final PurchaseInfo purchaseInfo);
    }
}
