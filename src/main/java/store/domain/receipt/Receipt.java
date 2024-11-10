package store.domain.receipt;

import java.util.List;
import store.common.dto.response.PromotionedProductResponse;
import store.common.dto.response.PurchaseCostResponse;
import store.common.dto.response.PurchasedProductResponse;
import store.domain.membership.Membership;
import store.domain.vo.PurchaseResult;

final public class Receipt {

    private final ProductReceipt productReceipt;
    private final PromotionReceipt promotionReceipt;
    private final CostReceipt costReceipt;

    public Receipt() {
        this.productReceipt = new ProductReceipt();
        this.promotionReceipt = new PromotionReceipt();
        this.costReceipt = new CostReceipt();
    }

    public void addPurchase(final PurchaseResult purchaseResult) {
        this.productReceipt.addPurchase(purchaseResult);
        this.promotionReceipt.addPurchase(purchaseResult);
        this.costReceipt.addPurchase(purchaseResult);
    }

    public void applyMembership(final Membership membership) {
        this.costReceipt.applyMembership(membership);
    }

    public List<PurchasedProductResponse> buildPurchasedProductResponses() {
        return this.productReceipt.buildPurchasedProductResponses();
    }

    public List<PromotionedProductResponse> buildPromotionedProductResponses() {
        return this.promotionReceipt.buildPromotionedProductResponses();
    }

    public PurchaseCostResponse buildPurchaseCostResponse() {
        return this.costReceipt.buildPurchaseCostResponse();
    }
}
