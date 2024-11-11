package store.domain.receipt;

import java.util.ArrayList;
import java.util.List;
import store.common.dto.response.PurchasedProductResponse;
import store.domain.vo.PurchaseResult;

final public class ProductReceipt {

    private final List<PurchasedProductResponse> purchasedProductResponses = new ArrayList<>();

    public void addPurchase(final PurchaseResult purchaseResult) {
        this.purchasedProductResponses.add(new PurchasedProductResponse(
                purchaseResult.productName(),
                purchaseResult.purchaseAmount(),
                purchaseResult.price() * purchaseResult.purchaseAmount()
        ));
    }

    public List<PurchasedProductResponse> buildPurchasedProductResponses() {
        return this.purchasedProductResponses;
    }
}
