package store.domain.receipt;

import java.util.ArrayList;
import java.util.List;
import store.common.dto.response.PurchaseCostResponse;
import store.domain.membership.Membership;
import store.domain.membership.NoMembership;
import store.domain.vo.PurchaseResult;

final public class CostReceipt {

    private final List<PurchaseResult> purchaseResults = new ArrayList<>();
    private Membership membership = new NoMembership();

    public void addPurchase(final PurchaseResult purchaseResult) {
        this.purchaseResults.add(purchaseResult);
    }

    public void applyMembership(final Membership membership) {
        this.membership = membership;
    }

    public PurchaseCostResponse buildPurchaseCostResponse() {
        int originalPurchaseCost = originalPurchaseCost();
        int promotionSaleCost = calculatePromotionSaleCost();
        int membershipSaleCost = membership.calculateMembershipSaleAmount(purchaseResults);
        return new PurchaseCostResponse(
                originalPurchaseCost,
                calculatePurchaseCount(),
                promotionSaleCost,
                membershipSaleCost,
                originalPurchaseCost - promotionSaleCost - membershipSaleCost
        );
    }

    private int originalPurchaseCost() {
        return purchaseResults.stream()
                .mapToInt(purchaseResult -> purchaseResult.purchaseAmount() * purchaseResult.price())
                .sum();
    }

    private int calculatePurchaseCount() {
        return purchaseResults.stream()
                .mapToInt(PurchaseResult::purchaseAmount)
                .sum();
    }

    private int calculatePromotionSaleCost() {
        return purchaseResults.stream()
                .mapToInt(purchaseResult -> purchaseResult.promotionGetAmount() * purchaseResult.price())
                .sum();
    }
}
