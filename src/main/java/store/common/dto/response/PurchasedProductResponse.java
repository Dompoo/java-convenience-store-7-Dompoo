package store.common.dto.response;

public record PurchasedProductResponse(
        String productName,
        int purchaseAmount,
        int originalPrice
) {
}
