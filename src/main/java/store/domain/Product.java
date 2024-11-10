package store.domain;

import store.common.ParamsValidator;
import store.common.exception.StoreExceptions;

final public class Product {

    private static final int MIN_PRICE = 0;

    private final String name;
    private final int price;
    private Stock stock;
    private final Promotion promotion;

    private Product(
            final String name,
            final int price,
            final Stock stock,
            final Promotion promotion
    ) {
        ParamsValidator.validateParamsNotNull(name, promotion);
        validate(name, price);
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotion = promotion;
    }

    public static Product of(
            final String name,
            final int price,
            final int defaultStock,
            final int promotionStock,
            final Promotion promotion
    ) {
        return new Product(name, price, new Stock(defaultStock, promotionStock), promotion);
    }

    private static void validate(final String name, final int price) {
        if (name.isBlank()) {
            throw StoreExceptions.ILLEGAL_ARGUMENT.get();
        }
        if (price < MIN_PRICE) {
            throw StoreExceptions.ILLEGAL_ARGUMENT.get();
        }
    }

    public void reduceStock(final int totalDecreaseStock, final int promotionDecreaseStock) {
        this.stock = this.stock.withReducing(totalDecreaseStock, promotionDecreaseStock);
    }

    public boolean canPurchaseWithPromotion() {
        if (!hasPromotion()) {
            return false;
        }
        return getPromotionStock() != 0;
    }

    public boolean isJustRightPromotionUnit(final int purchaseAmount) {
        if (!hasPromotion()) {
            return false;
        }
        return purchaseAmount % promotion.getPromotionUnit() == 0 && purchaseAmount <= getPromotionStock();
    }

    public boolean canGetFreeProduct(final int purchaseAmount) {
        if (!hasPromotion()) {
            return false;
        }
        int promotionUnit = promotion.getPromotionUnit();
        return purchaseAmount % promotionUnit >= promotion.getPromotionBuy()
                && (purchaseAmount / (promotionUnit) + 1) * (promotionUnit) <= getPromotionStock();
    }

    public boolean isPromotionStockLack(final int purchaseAmount) {
        if (!hasPromotion()) {
            return false;
        }
        int promotionUnit = promotion.getPromotionUnit();
        return purchaseAmount % promotionUnit < promotion.getPromotionBuy()
                || purchaseAmount > (getPromotionStock() / promotionUnit) * getPromotionStock();
    }

    public int calculateBringFreeProductCount(final int purchaseAmount) {
        if (!hasPromotion() || !canGetFreeProduct(purchaseAmount)) {
            return 0;
        }
        int promotionUnit = promotion.getPromotionUnit();
        return (((purchaseAmount / promotionUnit) + 1) * promotionUnit) - purchaseAmount;
    }

    public int calculateNoPromotionsProductCount(final int purchaseAmount) {
        int promotionUnit = promotion.getPromotionUnit();
        if (purchaseAmount < this.getPromotionStock()) {
            return purchaseAmount % promotionUnit;
        }
        return purchaseAmount - (this.getPromotionStock() / promotionUnit) * promotionUnit;
    }

    public boolean hasPromotion() {
        return this.promotion.hasPromotion();
    }

    public boolean canPurchase(final int purchaseAmount) {
        return this.stock.getTotalStock() >= purchaseAmount;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getDefaultStock() {
        return stock.getDefaultStock();
    }

    public int getPromotionStock() {
        return stock.getPromotionStock();
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
