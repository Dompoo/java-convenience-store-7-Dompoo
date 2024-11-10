package store.domain;

import java.util.Arrays;
import store.common.exception.StoreExceptions;

public enum DecisionType {

    FULL_DEFAULT((product, purchaseAmount) -> !product.canPurchaseWithPromotion()),
    FULL_PROMOTION(Product::isJustRightPromotionUnit),
    CAN_GET_FREE_PRODUCT(Product::canGetFreeProduct),
    PROMOTION_STOCK_LACK(Product::isPromotionStockLack),
    ;

    private final DecisionInfo decisionInfo;

    DecisionType(DecisionInfo decisionInfo) {
        this.decisionInfo = decisionInfo;
    }

    public static DecisionType of(final Product product, final int purchaseAmount) {
        validateStockSufficient(product, purchaseAmount);
        return Arrays.stream(DecisionType.values())
                .filter(decisionType -> decisionType.decisionInfo.decide(product, purchaseAmount))
                .findFirst()
                .orElse(PROMOTION_STOCK_LACK);
    }

    private static void validateStockSufficient(final Product product, final int purchaseAmount) {
        if (!product.canPurchase(purchaseAmount)) {
            throw StoreExceptions.PURCHASE_OVER_STOCK.get();
        }
    }

    @FunctionalInterface
    public interface DecisionInfo {
        boolean decide(final Product product, final int purchaseAmount);
    }
}
