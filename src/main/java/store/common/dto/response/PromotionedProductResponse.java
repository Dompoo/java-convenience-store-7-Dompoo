package store.common.dto.response;

public record PromotionedProductResponse(
        String productName,
        int promotionedAmount,
        int requestOrder
) implements Comparable<PromotionedProductResponse> {

    @Override
    public int compareTo(final PromotionedProductResponse o) {
        return this.requestOrder - o.requestOrder;
    }
}
