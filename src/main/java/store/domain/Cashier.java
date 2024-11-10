package store.domain;

final public class Cashier {

    public static PurchaseType decidePurchaseType(
            final Product product,
            final int purchaseAmount,
            final DecisionType decisionType,
            final DecisionSupplier<Boolean> bringFreeProductSupplier,
            final DecisionSupplier<Boolean> bringDefaultProductBackSupplier
    ) {
        if (decisionType == DecisionType.FULL_DEFAULT) {
            return PurchaseType.FULL_DEFAULT;
        }
        if (decisionType == DecisionType.FULL_PROMOTION) {
            return PurchaseType.FULL_PROMOTION;
        }
        if (decisionType == DecisionType.CAN_GET_FREE_PRODUCT) {
            return decideBringFreeProduct(product, purchaseAmount, bringFreeProductSupplier);
        }
        return decideBringDefaultProductBack(product, purchaseAmount, bringDefaultProductBackSupplier);
    }

    private static PurchaseType decideBringFreeProduct(
            final Product product,
            final int purchaseAmount,
            final DecisionSupplier<Boolean> bringFreeProductPredicate
    ) {
        if (bringFreeProductPredicate.get(product.getName(), product.calculateBringFreeProductCount(purchaseAmount))) {
            return PurchaseType.FULL_PROMOTION_BRING_FREE;
        }
        return PurchaseType.FULL_PROMOTION_NOT_BRING_FREE;
    }

    private static PurchaseType decideBringDefaultProductBack(
            final Product product,
            final int purchaseAmount,
            final DecisionSupplier<Boolean> bringDefaultProductBackPredicate
    ) {
        if (bringDefaultProductBackPredicate.get(
                product.getName(),
                product.calculateNoPromotionsProductCount(purchaseAmount))) {
            return PurchaseType.PORTION_PROMOTION_NOT_BRING_BACK;
        }
        return PurchaseType.PORTION_PROMOTION_BRING_BACK;
    }

    @FunctionalInterface
    public interface DecisionSupplier<T> {
        T get(String name, Integer count);
    }
}
