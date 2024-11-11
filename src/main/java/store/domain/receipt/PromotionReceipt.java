package store.domain.receipt;

import java.util.ArrayList;
import java.util.List;
import store.common.dto.response.PromotionedProductResponse;
import store.domain.vo.PurchaseResult;

final public class PromotionReceipt {

    private final List<PromotionedProductResponse> promotionedProductResponses = new ArrayList<>();

    public void addPurchase(final PurchaseResult purchaseResult) {
        if (purchaseResult.promotionGetAmount() == 0) {
            return;
        }
        promotionedProductResponses.add(new PromotionedProductResponse(
                purchaseResult.productName(),
                purchaseResult.promotionGetAmount(),
                purchaseResult.requestOrder()
        ));
    }

    public List<PromotionedProductResponse> buildPromotionedProductResponses() {
        return promotionedProductResponses;
    }
}
