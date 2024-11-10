package store.domain;

import java.time.LocalDate;
import store.common.ParamsValidator;
import store.common.exception.StoreExceptions;

final public class Promotion {

    private final String name;
    private final PromotionType promotionType;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(
            final String name,
            final PromotionType promotionType,
            final LocalDate startDate,
            final LocalDate endDate
    ) {
        ParamsValidator.validateParamsNotNull(name, promotionType, startDate, endDate);
        validate(startDate, endDate);
        this.name = name;
        this.promotionType = promotionType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private static void validate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw StoreExceptions.ILLEGAL_ARGUMENT.get();
        }
    }

    public boolean hasPromotion() {
        return promotionType != PromotionType.NO_PROMOTION;
    }

    public String getName() {
        return name;
    }

    public int getPromotionUnit() {
        return this.promotionType.getPromotionUnit();
    }

    public int getPromotionBuy() {
        return this.promotionType.getBuy();
    }

    public int getPromotionGet() {
        return this.promotionType.getGet();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
