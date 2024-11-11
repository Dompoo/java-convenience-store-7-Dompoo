package store.common.dto.request;

import java.util.Objects;

public record PurchaseRequest(
        String productName,
        int purchaseAmount,
        int requestOrder
) implements Comparable<PurchaseRequest> {

    public static PurchaseRequest getDefaultPurchaseRequest() {
        return new PurchaseRequest("", 0, Integer.MAX_VALUE);
    }

    public PurchaseRequest merge(final PurchaseRequest other) {
        return new PurchaseRequest(
                getMergedProductName(other.productName),
                this.purchaseAmount + other.purchaseAmount,
                Math.min(this.requestOrder, other.requestOrder)
        );
    }

    private String getMergedProductName(final String otherProductName) {
        if (Objects.isNull(this.productName) || this.productName.isBlank()) {
            return otherProductName;
        }
        return this.productName;
    }

    @Override
    public int compareTo(final PurchaseRequest o) {
        return this.requestOrder - o.requestOrder;
    }
}
