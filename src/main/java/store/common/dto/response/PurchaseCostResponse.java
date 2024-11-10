package store.common.dto.response;

public record PurchaseCostResponse(
        int originalPurchaseCost,
        int purchaseAmount,
        int promotionSaleCost,
        int membershipSaleCost,
        int finalPrice
) {
}
