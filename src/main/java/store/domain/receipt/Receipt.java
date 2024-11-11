package store.domain.receipt;

import java.util.List;
import store.common.dto.response.PromotionedProductResponse;
import store.common.dto.response.PurchaseCostResponse;
import store.common.dto.response.PurchasedProductResponse;
import store.domain.membership.Membership;
import store.domain.vo.PurchaseResult;

final public class Receipt {

    private final ProductReceipt productReceipt = new ProductReceipt();
    private final PromotionReceipt promotionReceipt = new PromotionReceipt();
    private final CostReceipt costReceipt = new CostReceipt();

    public void addPurchase(final PurchaseResult purchaseResult) {
        this.productReceipt.addPurchase(purchaseResult);
        this.promotionReceipt.addPurchase(purchaseResult);
        this.costReceipt.addPurchase(purchaseResult);
    }

    public void applyMembership(final Membership membership) {
        costReceipt.applyMembership(membership);
    }

    public List<PurchasedProductResponse> getPurchasedProductResponses() {
        return productReceipt.buildPurchasedProductResponses();
    }

    public List<PromotionedProductResponse> getPromotionedProductResponses() {
        return promotionReceipt.buildPromotionedProductResponses();
    }

    public PurchaseCostResponse getPurchaseCostResponse() {
        return costReceipt.buildPurchaseCostResponse();
    }
}
