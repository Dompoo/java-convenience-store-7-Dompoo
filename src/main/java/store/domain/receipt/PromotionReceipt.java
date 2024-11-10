package store.domain.receipt;

import java.util.ArrayList;
import java.util.List;
import store.common.dto.response.PromotionedProductResponse;
import store.domain.vo.PurchaseResult;

final public class PromotionReceipt {

    private final List<PurchaseResult> purchaseResults = new ArrayList<>();

    public void addPurchase(final PurchaseResult purchaseResult) {
        this.purchaseResults.add(purchaseResult);
    }

    public List<PromotionedProductResponse> buildPromotionedProductResponses() {
        List<PromotionedProductResponse> promotionGets = new ArrayList<>();
        for (PurchaseResult purchaseResult : this.purchaseResults) {
            String productName = purchaseResult.productName();
            int promotionGetAmount = purchaseResult.promotionGetAmount();
            if (promotionGetAmount != 0) {
                promotionGets.add(new PromotionedProductResponse(productName, promotionGetAmount));
            }
        }
        return promotionGets;
    }
}
