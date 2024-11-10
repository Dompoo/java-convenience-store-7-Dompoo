package store.common.dto.response;

public record PromotionedProductResponse(
        String productName,
        int promotionedAmount
) {
}
