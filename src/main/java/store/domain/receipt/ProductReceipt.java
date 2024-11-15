package store.domain.receipt;

import java.util.ArrayList;
import java.util.List;
import store.common.dto.response.PurchasedProductResponse;
import store.domain.vo.PurchaseResult;

final public class ProductReceipt {

    private final List<PurchasedProductResponse> purchasedProductResponses = new ArrayList<>();

    public void addPurchase(final PurchaseResult purchaseResult) {
        purchasedProductResponses.add(new PurchasedProductResponse(
                purchaseResult.productName(),
                purchaseResult.purchaseAmount(),
                purchaseResult.price() * purchaseResult.purchaseAmount(),
                purchaseResult.requestOrder()
        ));
    }

    public List<PurchasedProductResponse> buildPurchasedProductResponses() {
        return purchasedProductResponses;
    }
}
