package store.common.dto.response;

public record PurchasedProductResponse(
        String productName,
        int purchaseAmount,
        int unitPrice,
        int requestOrder
) implements Comparable<PurchasedProductResponse> {

    @Override
    public int compareTo(final PurchasedProductResponse o) {
        return this.requestOrder - o.requestOrder;
    }
}
