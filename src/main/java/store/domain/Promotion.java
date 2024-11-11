package store.domain;

import store.common.ParamsValidator;

final public class Promotion {

    private static final String NO_PROMOTION_NAME = "";

    private final String name;
    private final PromotionType promotionType;

    private Promotion(
            final String name,
            final PromotionType promotionType
    ) {
        ParamsValidator.validateParamsNotNull(name, promotionType);
        this.name = name;
        this.promotionType = promotionType;
    }

    public static Promotion of(
            final String name,
            final int buy,
            final int get
    ) {
        return new Promotion(name, PromotionType.of(buy, get));
    }

    public static Promotion getNoPromotion() {
        return new Promotion(NO_PROMOTION_NAME, PromotionType.NO_PROMOTION);
    }

    public boolean hasPromotion() {
        return promotionType != PromotionType.NO_PROMOTION;
    }

    public String getName() {
        return name;
    }

    public int getPromotionUnit() {
        return promotionType.getPromotionUnit();
    }

    public int getPromotionBuy() {
        return promotionType.getBuy();
    }

    public int getPromotionGet() {
        return promotionType.getGet();
    }
}
