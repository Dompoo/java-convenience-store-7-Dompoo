package store.domain.receipt;

import java.util.ArrayList;
import java.util.List;
import store.common.dto.response.PurchasedProductResponse;
import store.domain.vo.PurchaseResult;

final public class ProductReceipt {

    private final List<PurchaseResult> purchaseResults = new ArrayList<>();

    public void addPurchase(final PurchaseResult purchaseResult) {
        this.purchaseResults.add(purchaseResult);
    }

    public List<PurchasedProductResponse> buildPurchasedProductResponses() {
        List<PurchasedProductResponse> purchasedProducts = new ArrayList<>();
        for (PurchaseResult purchaseResult : this.purchaseResults) {
            purchasedProducts.add(new PurchasedProductResponse(
                    purchaseResult.productName(),
                    purchaseResult.purchaseAmount(),
                    purchaseResult.price() * purchaseResult.purchaseAmount()
            ));
        }
        return purchasedProducts;
    }
}
