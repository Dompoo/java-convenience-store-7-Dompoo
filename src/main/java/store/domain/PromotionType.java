package store.domain;

import java.util.Arrays;

public enum PromotionType {

    NO_PROMOTION(0, 0),
    BUY_ONE_GET_ONE(1, 1),
    BUY_TWO_GET_ONE(2, 1),
    ;

    private final int buy;
    private final int get;

    PromotionType(int buy, int get) {
        this.buy = buy;
        this.get = get;
    }

    public static PromotionType of(final int buy, final int get) {
        return Arrays.stream(PromotionType.values())
                .filter(promotionType -> promotionType.buy == buy && promotionType.get == get)
                .findFirst()
                .orElse(PromotionType.NO_PROMOTION);
    }

    public int getPromotionUnit() {
        return buy + get;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }
}
