package store.domain.vo;

public record PurchaseStatus(
        int finalPurchaseAmount,
        int promotionGetAmount,
        int decreasePromotionStock,
        int promotionedProductAmount
) {
}
