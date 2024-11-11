package store.domain.vo;

import store.domain.Product;

public record PurchaseResult(
        String productName,
        int purchaseAmount,
        int promotionedProductAmount,
        int price,
        int promotionGetAmount,
        int requestOrder
) {
    public static PurchaseResult of(
            final Product product,
            final PurchaseStatus purchaseStatus,
            final int requestOrder
    ) {
        return new PurchaseResult(
                product.getName(),
                purchaseStatus.finalPurchaseAmount(),
                purchaseStatus.promotionedProductAmount(),
                product.getPrice(),
                purchaseStatus.promotionGetAmount(),
                requestOrder
        );
    }

    public int getDefaultPurchasePrice() {
        return (purchaseAmount - promotionedProductAmount) * price;
    }
}
