package store.common.dto.response;

public record PurchaseCostResponse(
        int originalPurchaseCost,
        int totalPurchasedAmount,
        int promotionSaleCost,
        int membershipSaleCost,
        int finalPrice
) {
}
